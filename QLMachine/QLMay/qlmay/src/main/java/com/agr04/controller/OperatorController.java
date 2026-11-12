package com.agr04.controller;

import com.agr04.model.Operators;
import com.agr04.model.Users;
import com.agr04.repository.BookingRepository;
import com.agr04.repository.OperatorRepository;
import com.agr04.repository.UserRepository;
import com.agr04.service.SessionIdentity;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/operators")
public class OperatorController {
    private final OperatorRepository operatorRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final SessionIdentity identity;

    public OperatorController(OperatorRepository operatorRepository, UserRepository userRepository,
                              BookingRepository bookingRepository, SessionIdentity identity) {
        this.operatorRepository = operatorRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.identity = identity;
    }

    @GetMapping
    public List<Operators> getOperators(@RequestParam(required = false) LocalDateTime startTime,
                                        @RequestParam(required = false) LocalDateTime endTime,
                                        HttpSession session) {
        Integer userId = identity.requireUserId(session);
        String role = identity.requireRole(session);
        if (role.equals("FARMER")) {
            if ((startTime == null) != (endTime == null)
                    || (startTime != null && !endTime.isAfter(startTime))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khoảng thời gian tìm tài xế không hợp lệ.");
            }
            return operatorRepository.findByIsEligibleTrue().stream()
                    .filter(operator -> startTime == null
                            || bookingRepository.countOverlappingOperatorBookings(
                            operator.getOperatorId(), startTime, endTime) == 0)
                    .toList();
        }
        if (role.equals("OPERATOR")) {
            return operatorRepository.findByUser_UserId(userId).stream().toList();
        }
        identity.requireRole(session, "OWNER", "ADMIN");
        return operatorRepository.findAll();
    }

    @PostMapping
    public Operators registerOperator(@RequestBody Operators details, HttpSession session) {
        Integer userId = identity.requireRole(session, "OPERATOR");
        if (details.getLicenseNo() == null || details.getLicenseNo().isBlank()
                || details.getHourlyRate() == null || details.getHourlyRate().signum() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Số bằng cấp và đơn giá không âm là bắt buộc.");
        }
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tài khoản không còn tồn tại."));
        Operators operator = operatorRepository.findByUser_UserId(userId).orElseGet(Operators::new);
        operator.setUser(user);
        operator.setLicenseNo(details.getLicenseNo().trim());
        operator.setHourlyRate(details.getHourlyRate());
        operator.setIsEligible(false);
        return operatorRepository.save(operator);
    }

    @PutMapping("/{id}/status")
    public Operators updateStatus(@PathVariable Integer id, @RequestParam Boolean eligible,
                                  HttpSession session) {
        identity.requireRole(session, "ADMIN");
        Operators operator = operatorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy tài xế."));
        operator.setIsEligible(eligible);
        return operatorRepository.save(operator);
    }
}
