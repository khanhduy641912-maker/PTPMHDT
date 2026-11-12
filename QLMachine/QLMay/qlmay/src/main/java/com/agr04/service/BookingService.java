package com.agr04.service;

import com.agr04.model.Bookings;
import com.agr04.model.Machines;
import com.agr04.model.Operators;
import com.agr04.repository.BookingRepository;
import com.agr04.repository.MachineRepository;
import com.agr04.repository.OperatorRepository;
import com.agr04.repository.MaintenanceRepository;
import com.agr04.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final MachineRepository machineRepository;
    private final OperatorRepository operatorRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final UserRepository userRepository;
    private final MachineStatusService machineStatusService;

    public BookingService(BookingRepository bookingRepository,
                          MachineRepository machineRepository,
                          OperatorRepository operatorRepository,
                          MaintenanceRepository maintenanceRepository,
                          UserRepository userRepository,
                          MachineStatusService machineStatusService) {
        this.bookingRepository = bookingRepository;
        this.machineRepository = machineRepository;
        this.operatorRepository = operatorRepository;
        this.maintenanceRepository = maintenanceRepository;
        this.userRepository = userRepository;
        this.machineStatusService = machineStatusService;
    }

    @Transactional
    public Bookings createBooking(Bookings booking, Integer farmerId) {
        if (booking.getMachine() == null || booking.getMachine().getMachineId() == null) {
            throw badRequest("Vui lòng chọn máy cần thuê.");
        }
        if (booking.getStartTime() == null || booking.getEndTime() == null) {
            throw badRequest("Vui lòng chọn thời gian bắt đầu và kết thúc.");
        }
        if (!booking.getEndTime().isAfter(booking.getStartTime())) {
            throw badRequest("Thời gian kết thúc phải lớn hơn thời gian bắt đầu.");
        }
        long minutes = java.time.Duration.between(booking.getStartTime(), booking.getEndTime()).toMinutes();
        if (minutes < 60) {
            throw badRequest("Thời gian thuê tối thiểu là 1 giờ.");
        }

        Machines machine = machineRepository.findById(booking.getMachine().getMachineId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy máy nông nghiệp!"));
        if (machine.getHourlyRate() == null || machine.getHourlyRate().signum() <= 0) {
            throw badRequest("Máy chưa có đơn giá thuê hợp lệ.");
        }

        long maintenanceOverlaps = maintenanceRepository.countOverlappingMaintenances(
                machine.getMachineId(), booking.getStartTime(), booking.getEndTime());
        if (maintenanceOverlaps > 0) {
            throw badRequest("Máy đang có lịch bảo trì trong khoảng thời gian này.");
        }

        BigDecimal operatorRate = BigDecimal.ZERO;
        Operators selectedOperator = null;
        if (booking.getOperator() != null && booking.getOperator().getOperatorId() != null) {
            Operators op = operatorRepository.findById(booking.getOperator().getOperatorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy thông tin tài xế!"));
            if (!Boolean.TRUE.equals(op.getIsEligible())) {
                throw badRequest("Tài xế chưa đủ điều kiện hoặc chưa được duyệt bằng cấp.");
            }
            if (op.getHourlyRate() == null || op.getHourlyRate().signum() < 0) {
                throw badRequest("Đơn giá tài xế không hợp lệ.");
            }
            if (bookingRepository.countOverlappingOperatorBookings(
                    op.getOperatorId(), booking.getStartTime(), booking.getEndTime()) > 0) {
                throw badRequest("Tài xế đã có lịch làm việc trùng thời gian này.");
            }
            operatorRate = op.getHourlyRate();
            selectedOperator = op;
        }

        long overlaps = bookingRepository.countOverlappingBookings(
                machine.getMachineId(),
                booking.getStartTime(),
                booking.getEndTime()
        );
        if (overlaps > 0) {
            throw badRequest("Máy đã có đơn đặt trùng trong khoảng thời gian này.");
        }

        if (booking.getStartTime().isBefore(LocalDateTime.now())) {
            throw badRequest("Thời gian bắt đầu không được nằm trong quá khứ.");
        }

        long halfHourBlocks = (minutes + 29) / 30;
        BigDecimal billableHours = BigDecimal.valueOf(halfHourBlocks)
                .divide(BigDecimal.valueOf(2), 1, RoundingMode.UNNECESSARY);
        BigDecimal totalFee = machine.getHourlyRate().add(operatorRate)
                .multiply(billableHours).setScale(2, RoundingMode.HALF_UP);
        booking.setFarmer(userRepository.findById(farmerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tài khoản không còn tồn tại.")));
        booking.setMachine(machine);
        booking.setOperator(selectedOperator);
        if (booking.getFieldAddress() != null && booking.getFieldAddress().length() > 255) {
            throw badRequest("Địa chỉ khu vực làm việc không được vượt quá 255 ký tự.");
        }
        booking.setTotalFee(totalFee);
        booking.setStatus("PENDING");

        Bookings saved = bookingRepository.saveAndFlush(booking);
        machineStatusService.refresh(machine.getMachineId());
        return saved;
    }

    @Transactional
    public Bookings confirm(Integer bookingId, Integer ownerId, boolean admin) {
        Bookings booking = requireBooking(bookingId);
        if (!admin) {
            requireOwner(booking, ownerId);
        }
        requireStatus(booking, "PENDING", "Chỉ có thể duyệt đơn đang chờ.");
        booking.setStatus("CONFIRMED");
        Bookings saved = bookingRepository.save(booking);
        machineStatusService.refresh(booking.getMachine().getMachineId());
        return saved;
    }

    @Transactional
    public Bookings reject(Integer bookingId, Integer ownerId, boolean admin) {
        Bookings booking = requireBooking(bookingId);
        if (!admin) {
            requireOwner(booking, ownerId);
        }
        requireStatus(booking, "PENDING", "Chỉ có thể từ chối đơn đang chờ.");
        booking.setStatus("CANCELLED");
        Bookings saved = bookingRepository.save(booking);
        machineStatusService.refresh(booking.getMachine().getMachineId());
        return saved;
    }

    @Transactional
    public Bookings cancel(Integer bookingId, Integer farmerId) {
        Bookings booking = requireBooking(bookingId);
        if (!booking.getFarmer().getUserId().equals(farmerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn chỉ có thể hủy đơn do mình tạo.");
        }
        requireStatus(booking, "PENDING", "Chỉ có thể hủy đơn đang chờ chủ máy duyệt.");
        booking.setStatus("CANCELLED");
        Bookings saved = bookingRepository.save(booking);
        machineStatusService.refresh(booking.getMachine().getMachineId());
        return saved;
    }

    public Bookings requireBooking(Integer bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn đặt máy."));
    }

    void requireOwner(Bookings booking, Integer userId) {
        if (booking.getMachine().getOwner() == null
                || !booking.getMachine().getOwner().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không sở hữu máy của đơn này.");
        }
    }

    private void requireStatus(Bookings booking, String expected, String message) {
        if (!expected.equals(booking.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, message);
        }
    }

    private ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}