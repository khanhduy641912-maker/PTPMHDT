package com.agr04.controller;

import com.agr04.model.Maintenances;
import com.agr04.repository.MaintenanceRepository;
import com.agr04.service.MaintenanceService;
import com.agr04.service.SessionIdentity;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/maintenances")
public class MaintenanceController {
    private final MaintenanceRepository maintenanceRepository;
    private final MaintenanceService maintenanceService;
    private final SessionIdentity identity;

    public MaintenanceController(MaintenanceRepository maintenanceRepository,
                                 MaintenanceService maintenanceService,
                                 SessionIdentity identity) {
        this.maintenanceRepository = maintenanceRepository;
        this.maintenanceService = maintenanceService;
        this.identity = identity;
    }

    @GetMapping
    public List<Maintenances> getMaintenances(HttpSession session) {
        String role = identity.requireRole(session);
        if ("ADMIN".equals(role)) {
            return maintenanceRepository.findAll();
        }
        identity.requireRole(session, "OWNER");
        return maintenanceRepository.findByMachine_Owner_UserIdOrderByStartTimeAsc(
                identity.requireUserId(session));
    }

    @PostMapping
    public Maintenances schedule(@RequestBody Maintenances maintenance, HttpSession session) {
        Integer ownerId = identity.requireRole(session, "OWNER");
        return maintenanceService.schedule(maintenance, ownerId);
    }

    @PutMapping("/{id}/complete")
    public Maintenances complete(@PathVariable Integer id, HttpSession session) {
        Integer userId = identity.requireRole(session, "OWNER", "ADMIN");
        return maintenanceService.complete(id, userId, identity.hasRole(session, "ADMIN"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Integer id, HttpSession session) {
        Integer ownerId = identity.requireRole(session, "OWNER");
        maintenanceService.cancel(id, ownerId);
    }
}
