package com.agr04.service;

import com.agr04.dto.HandoverRequest;
import com.agr04.model.Bookings;
import com.agr04.model.Handovers;
import com.agr04.repository.BookingRepository;
import com.agr04.repository.HandoverRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class HandoverService {
    private final BookingService bookingService;
    private final HandoverRepository handoverRepository;
    private final BookingRepository bookingRepository;
    private final SessionIdentity identity;
    private final ObjectMapper objectMapper;
    private final MachineStatusService machineStatusService;

    public HandoverService(BookingService bookingService, HandoverRepository handoverRepository,
                           BookingRepository bookingRepository,
                           SessionIdentity identity, ObjectMapper objectMapper,
                           MachineStatusService machineStatusService) {
        this.bookingService = bookingService;
        this.handoverRepository = handoverRepository;
        this.bookingRepository = bookingRepository;
        this.identity = identity;
        this.objectMapper = objectMapper;
        this.machineStatusService = machineStatusService;
    }

    @Transactional
    public Handovers create(Integer bookingId, HandoverRequest request,
                            jakarta.servlet.http.HttpSession session) {
        Integer userId = identity.requireUserId(session);
        String role = identity.requireRole(session);
        Bookings booking = bookingService.requireBooking(bookingId);
        requireParticipant(booking, userId, role);
        if (!"FARMER".equals(role) && !"OWNER".equals(role) && !"OPERATOR".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Chỉ các bên tham gia bàn giao mới được ký.");
        }

        String type = request.type() == null ? "" : request.type().toUpperCase();
        if (!"CHECK_IN".equals(type) && !"CHECK_OUT".equals(type)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loại biên bản phải là CHECK_IN hoặc CHECK_OUT.");
        }
        if (request.odometer() == null || request.odometer() < 0
                || request.fuelPercent() == null || request.fuelPercent() < 0 || request.fuelPercent() > 100
                || request.machineCondition() == null || request.machineCondition().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cần nhập chỉ số đồng hồ, nhiên liệu từ 0 đến 100% và tình trạng máy.");
        }
        if ("CHECK_IN".equals(type)) {
            if (!"CONFIRMED".equals(booking.getStatus())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Chỉ được nhận máy khi đơn đã được chủ máy duyệt.");
            }
        } else {
            if (!"IN_PROGRESS".equals(booking.getStatus())
                    || !handoverRepository.existsByBooking_BookingIdAndType(bookingId, "CHECK_IN")) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Chỉ được trả máy sau khi đã lập biên bản CHECK_IN.");
            }
        }
        if (handoverRepository.existsByBooking_BookingIdAndType(bookingId, type)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Biên bản đã được lập; bên còn lại cần xác nhận trên biên bản hiện có.");
        }

        Handovers handover = new Handovers();
        handover.setBooking(booking);
        handover.setHandoverTime(LocalDateTime.now());
        handover.setType(type);
        handover.setMachineCondition(serializeCondition(request));
        handover.setRemarks(serializeSigners(List.of(signer(session, userId, role)), request.remarks()));
        Handovers saved = handoverRepository.saveAndFlush(handover);

        bookingRepository.updateStatus(bookingId, "CHECK_IN".equals(type) ? "CONFIRMED" : "IN_PROGRESS");
        machineStatusService.refresh(booking.getMachine().getMachineId());
        return saved;
    }

    @Transactional
    public Handovers sign(Integer bookingId, Integer handoverId,
                          jakarta.servlet.http.HttpSession session) {
        Integer userId = identity.requireUserId(session);
        String role = identity.requireRole(session);
        Bookings booking = bookingService.requireBooking(bookingId);
        requireParticipant(booking, userId, role);
        if (!"FARMER".equals(role) && !"OWNER".equals(role) && !"OPERATOR".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Chỉ các bên tham gia bàn giao mới được ký.");
        }

        Handovers handover = handoverRepository.findByHandoverIdAndBooking_BookingId(handoverId, bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy biên bản."));
        Map<String, Object> metadata = parseSigners(handover.getRemarks());
        List<Map<String, Object>> signers = signerList(metadata);
        if (signers.size() != 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Biên bản không ở trạng thái chờ chữ ký.");
        }
        Map<String, Object> firstSigner = signers.get(0);
        if (String.valueOf(firstSigner.get("userId")).equals(String.valueOf(userId))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Chữ ký cần đến từ bên còn lại.");
        }
        String firstRole = String.valueOf(firstSigner.get("role"));
        boolean complementary = ("FARMER".equals(firstRole) && ("OWNER".equals(role) || "OPERATOR".equals(role)))
                || (("OWNER".equals(firstRole) || "OPERATOR".equals(firstRole)) && "FARMER".equals(role));
        if (!complementary) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cần chữ ký xác nhận từ phía còn lại của giao dịch.");
        }

        signers.add(signer(session, userId, role));
        handover.setRemarks(serializeSigners(signers, String.valueOf(metadata.getOrDefault("remarks", ""))));
        Handovers saved = handoverRepository.save(handover);
        if ("CHECK_IN".equals(handover.getType())) {
            bookingRepository.updateStatus(bookingId, "IN_PROGRESS");
            machineStatusService.refresh(booking.getMachine().getMachineId());
        } else {
            bookingRepository.updateStatus(bookingId, "COMPLETED");
            machineStatusService.refresh(booking.getMachine().getMachineId());
        }
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Handovers> list(Integer bookingId, jakarta.servlet.http.HttpSession session) {
        Integer userId = identity.requireUserId(session);
        String role = identity.requireRole(session);
        Bookings booking = bookingService.requireBooking(bookingId);
        requireParticipant(booking, userId, role);
        return handoverRepository.findByBooking_BookingIdOrderByHandoverTimeAsc(bookingId);
    }

    private void requireParticipant(Bookings booking, Integer userId, String role) {
        boolean participant = "ADMIN".equals(role)
                || ("FARMER".equals(role) && booking.getFarmer().getUserId().equals(userId))
                || ("OWNER".equals(role) && booking.getMachine().getOwner().getUserId().equals(userId))
                || ("OPERATOR".equals(role) && booking.getOperator() != null
                    && booking.getOperator().getUser().getUserId().equals(userId));
        if (!participant) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không tham gia đơn đặt máy này.");
        }
    }

    private String serializeCondition(HandoverRequest request) {
        Map<String, Object> condition = new LinkedHashMap<>();
        condition.put("odometer", request.odometer());
        condition.put("fuelPercent", request.fuelPercent());
        condition.put("condition", request.machineCondition().trim());
        try {
            return objectMapper.writeValueAsString(condition);
        } catch (JsonProcessingException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Không thể ghi nhận tình trạng máy.", exception);
        }
    }

    private Map<String, Object> signer(jakarta.servlet.http.HttpSession session, Integer userId, String role) {
        Map<String, Object> signer = new LinkedHashMap<>();
        signer.put("userId", userId);
        signer.put("name", session.getAttribute("fullName"));
        signer.put("role", role);
        signer.put("signedAt", LocalDateTime.now().toString());
        return signer;
    }

    private String serializeSigners(List<Map<String, Object>> signers, String remarks) {
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("signers", signers);
        metadata.put("remarks", remarks == null ? "" : remarks.trim());
        try {
            return objectMapper.writeValueAsString(metadata);
        } catch (JsonProcessingException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Không thể ghi nhận xác nhận bàn giao.", exception);
        }
    }

    private Map<String, Object> parseSigners(String value) {
        try {
            return objectMapper.readValue(value, new TypeReference<>() {});
        } catch (JsonProcessingException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Dữ liệu chữ ký của biên bản cũ không hợp lệ.", exception);
        }
    }

    private List<Map<String, Object>> signerList(Map<String, Object> metadata) {
        Object value = metadata.get("signers");
        if (!(value instanceof List<?> entries)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Biên bản chưa có chữ ký hợp lệ.");
        }
        List<Map<String, Object>> signers = new ArrayList<>();
        for (Object entry : entries) {
            if (!(entry instanceof Map<?, ?> signer)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Dữ liệu chữ ký của biên bản không hợp lệ.");
            }
            Map<String, Object> copy = new LinkedHashMap<>();
            signer.forEach((key, signerValue) -> copy.put(String.valueOf(key), signerValue));
            signers.add(copy);
        }
        return signers;
    }
}
