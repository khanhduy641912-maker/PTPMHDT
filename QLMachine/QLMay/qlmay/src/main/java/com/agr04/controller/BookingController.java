package com.agr04.controller;

import com.agr04.model.Bookings;
import com.agr04.repository.BookingRepository;
import com.agr04.service.BookingService;
import com.agr04.service.SessionIdentity;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;
    private final SessionIdentity identity;

    public BookingController(BookingService bookingService, BookingRepository bookingRepository,
                             SessionIdentity identity) {
        this.bookingService = bookingService;
        this.bookingRepository = bookingRepository;
        this.identity = identity;
    }

    @GetMapping
    public List<Bookings> getBookings(HttpSession session) {
        Integer userId = identity.requireUserId(session);
        return switch (identity.requireRole(session)) {
            case "ADMIN" -> bookingRepository.findAll();
            case "OWNER" -> bookingRepository.findByMachine_Owner_UserIdOrderByStartTimeDesc(userId);
            case "OPERATOR" -> bookingRepository.findByOperator_User_UserIdOrderByStartTimeDesc(userId);
            case "FARMER" -> bookingRepository.findByFarmer_UserIdOrderByStartTimeDesc(userId);
            default -> throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vai trò không hợp lệ.");
        };
    }

    @GetMapping("/{id}")
    public Bookings getBooking(@PathVariable Integer id, HttpSession session) {
        Integer userId = identity.requireUserId(session);
        String role = identity.requireRole(session);
        Bookings booking = bookingService.requireBooking(id);
        boolean permitted = "ADMIN".equals(role)
                || ("FARMER".equals(role) && booking.getFarmer().getUserId().equals(userId))
                || ("OWNER".equals(role) && booking.getMachine().getOwner().getUserId().equals(userId))
                || ("OPERATOR".equals(role) && booking.getOperator() != null
                    && booking.getOperator().getUser().getUserId().equals(userId));
        if (!permitted) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xem đơn đặt máy này.");
        }
        return booking;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Bookings createBooking(@RequestBody Bookings booking, HttpSession session) {
        Integer farmerId = identity.requireRole(session, "FARMER");
        return bookingService.createBooking(booking, farmerId);
    }

    @PutMapping("/{id}/confirm")
    public Bookings confirmBooking(@PathVariable Integer id, HttpSession session) {
        Integer userId = identity.requireRole(session, "OWNER", "ADMIN");
        return bookingService.confirm(id, userId, identity.hasRole(session, "ADMIN"));
    }

    @PutMapping("/{id}/reject")
    public Bookings rejectBooking(@PathVariable Integer id, HttpSession session) {
        Integer userId = identity.requireRole(session, "OWNER", "ADMIN");
        return bookingService.reject(id, userId, identity.hasRole(session, "ADMIN"));
    }

    @PutMapping("/{id}/cancel")
    public Bookings cancelBooking(@PathVariable Integer id, HttpSession session) {
        Integer userId = identity.requireRole(session, "FARMER");
        return bookingService.cancel(id, userId);
    }
}
