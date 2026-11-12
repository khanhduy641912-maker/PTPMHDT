package com.agr04.controller;

import com.agr04.model.Machines;
import com.agr04.model.Users;
import com.agr04.repository.MachineRepository;
import com.agr04.repository.BookingRepository;
import com.agr04.repository.MaintenanceRepository;
import com.agr04.repository.UserRepository;
import com.agr04.service.SessionIdentity;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/machines")
public class MachineController {
    private final MachineRepository machineRepository;
    private final UserRepository userRepository;
    private final SessionIdentity identity;
    private final BookingRepository bookingRepository;
    private final MaintenanceRepository maintenanceRepository;

    public MachineController(MachineRepository machineRepository, UserRepository userRepository,
                             SessionIdentity identity, BookingRepository bookingRepository,
                             MaintenanceRepository maintenanceRepository) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
        this.identity = identity;
        this.bookingRepository = bookingRepository;
        this.maintenanceRepository = maintenanceRepository;
    }

    @GetMapping
    public List<Machines> getMachines(@RequestParam(required = false) String keyword,
                                      @RequestParam(required = false) String type,
                                      HttpSession session) {
        String role = identity.requireRole(session);
        Integer userId = identity.requireUserId(session);
        List<Machines> machines = role.equals("OWNER")
                ? machineRepository.findByOwner_UserIdOrderByMachineIdDesc(userId)
                : machineRepository.findAll();
        String search = keyword == null ? "" : keyword.trim().toLowerCase(Locale.ROOT);
        String selectedType = type == null ? "" : type.trim().toLowerCase(Locale.ROOT);
        return machines.stream()
                .filter(machine -> search.isEmpty()
                        || machine.getName().toLowerCase(Locale.ROOT).contains(search)
                        || machine.getType().toLowerCase(Locale.ROOT).contains(search))
                .filter(machine -> selectedType.isEmpty()
                        || machine.getType().toLowerCase(Locale.ROOT).equals(selectedType))
                .toList();
    }

    @GetMapping("/{id}")
    public Machines getMachine(@PathVariable Integer id, HttpSession session) {
        identity.requireRole(session);
        return machineRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy máy."));
    }

    @GetMapping("/{id}/availability")
    public Map<String, Object> checkAvailability(@PathVariable Integer id,
                                                 @RequestParam LocalDateTime startTime,
                                                 @RequestParam LocalDateTime endTime,
                                                 HttpSession session) {
        identity.requireRole(session, "FARMER", "OWNER", "ADMIN");
        Machines machine = machineRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy máy."));
        if (!endTime.isAfter(startTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thời gian kết thúc phải sau thời gian bắt đầu.");
        }
        if (maintenanceRepository.countOverlappingMaintenances(id, startTime, endTime) > 0) {
            return Map.of("available", false, "reason", "Máy có lịch bảo trì trùng thời gian.");
        }
        if (bookingRepository.countOverlappingBookings(id, startTime, endTime) > 0) {
            return Map.of("available", false, "reason", "Máy đã có đơn đặt trùng thời gian.");
        }
        return Map.of("available", true, "reason", "Khung giờ hiện chưa có lịch xung đột.");
    }

    @PostMapping
    public Machines createMachine(@RequestBody Machines machine, HttpSession session) {
        Integer userId = identity.requireRole(session, "OWNER", "ADMIN");
        validateMachine(machine);
        if (identity.hasRole(session, "OWNER")) {
            machine.setOwner(userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tài khoản không còn tồn tại.")));
        } else {
            Integer ownerId = machine.getOwner() == null ? null : machine.getOwner().getUserId();
            Users owner = ownerId == null ? null : userRepository.findById(ownerId).orElse(null);
            if (owner == null || !"OWNER".equals(owner.getRole())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin cần chọn tài khoản chủ máy hợp lệ.");
            }
            machine.setOwner(owner);
        }
        machine.setStatus("AVAILABLE");
        return machineRepository.save(machine);
    }

    @PutMapping("/{id}")
    public Machines updateMachine(@PathVariable Integer id, @RequestBody Machines details,
                                  HttpSession session) {
        Integer userId = identity.requireRole(session, "OWNER", "ADMIN");
        Machines machine = machineRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy máy."));
        if (identity.hasRole(session, "OWNER")
                && !machine.getOwner().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không sở hữu máy này.");
        }
        if (details.getName() != null && !details.getName().isBlank()) {
            machine.setName(details.getName().trim());
        }
        if (details.getType() != null && !details.getType().isBlank()) {
            machine.setType(details.getType().trim());
        }
        if (details.getHourlyRate() != null) {
            if (details.getHourlyRate().signum() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Đơn giá máy phải lớn hơn 0.");
            }
            machine.setHourlyRate(details.getHourlyRate());
        }
        return machineRepository.save(machine);
    }

    private void validateMachine(Machines machine) {
        if (machine.getName() == null || machine.getName().isBlank()
                || machine.getType() == null || machine.getType().isBlank()
                || machine.getHourlyRate() == null || machine.getHourlyRate().signum() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Tên, loại máy và đơn giá lớn hơn 0 là bắt buộc.");
        }
    }
}
