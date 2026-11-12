package com.agr04.controller;

import com.agr04.dto.HandoverRequest;
import com.agr04.model.Handovers;
import com.agr04.service.HandoverService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings/{bookingId}/handovers")
public class HandoverController {
    private final HandoverService handoverService;

    public HandoverController(HandoverService handoverService) {
        this.handoverService = handoverService;
    }

    @GetMapping
    public List<Handovers> list(@PathVariable Integer bookingId, HttpSession session) {
        return handoverService.list(bookingId, session);
    }

    @PostMapping
    public Handovers create(@PathVariable Integer bookingId,
                           @RequestBody HandoverRequest request,
                           HttpSession session) {
        return handoverService.create(bookingId, request, session);
    }

    @PostMapping("/{handoverId}/sign")
    public Handovers sign(@PathVariable Integer bookingId, @PathVariable Integer handoverId,
                          HttpSession session) {
        return handoverService.sign(bookingId, handoverId, session);
    }
}
