package com.agr04.service;

import com.agr04.model.Machines;
import com.agr04.model.Maintenances;
import com.agr04.repository.BookingRepository;
import com.agr04.repository.MachineRepository;
import com.agr04.repository.MaintenanceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MaintenanceService {
    private final MaintenanceRepository maintenanceRepository;
    private final MachineRepository machineRepository;
    private final BookingRepository bookingRepository;
    private final MachineStatusService machineStatusService;

    public MaintenanceService(MaintenanceRepository maintenanceRepository,
                              MachineRepository machineRepository,
                              BookingRepository bookingRepository,
                              MachineStatusService machineStatusService) {
        this.maintenanceRepository = maintenanceRepository;
        this.machineRepository = machineRepository;
        this.bookingRepository = bookingRepository;
        this.machineStatusService = machineStatusService;
    }

    @Transactional
    public Maintenances schedule(Maintenances maintenance, Integer ownerId) {
        if (maintenance.getMachine() == null || maintenance.getMachine().getMachineId() == null
                || maintenance.getStartTime() == null || maintenance.getEndTime() == null
                || !maintenance.getEndTime().isAfter(maintenance.getStartTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cần chọn máy và thời gian bảo trì hợp lệ (kết thúc sau bắt đầu).");
        }
        Machines machine = machineRepository.findById(maintenance.getMachine().getMachineId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy máy."));
        if (machine.getOwner() == null || !machine.getOwner().getUserId().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không sở hữu máy này.");
        }
        LocalDateTime now = LocalDateTime.now();
        if (!maintenance.getEndTime().isAfter(now)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thời gian kết thúc bảo trì phải ở tương lai.");
        }
        if (maintenanceRepository.countOverlappingMaintenances(
                machine.getMachineId(), maintenance.getStartTime(), maintenance.getEndTime()) > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Máy đã có lịch bảo trì trùng thời gian.");
        }
        if (bookingRepository.countActiveBookingsOverlapping(
                machine.getMachineId(), maintenance.getStartTime(), maintenance.getEndTime()) > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Máy đã có đơn đặt trong khoảng này; hãy xử lý các đơn trước khi lập lịch bảo trì.");
        }

        maintenance.setMachine(machine);
        maintenance.setStatus(maintenance.getStartTime().isAfter(now) ? "SCHEDULED" : "IN_PROGRESS");
        Maintenances saved = maintenanceRepository.save(maintenance);
        machineStatusService.refresh(machine.getMachineId());
        return saved;
    }

    @Transactional
    public Maintenances complete(Integer maintenanceId, Integer ownerId, boolean admin) {
        Maintenances maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy lịch bảo trì."));
        if (!admin && !maintenance.getMachine().getOwner().getUserId().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không sở hữu máy này.");
        }
        if (!"IN_PROGRESS".equals(maintenance.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Chỉ có thể hoàn tất lịch bảo trì đang thực hiện.");
        }
        maintenance.setStatus("COMPLETED");
        Maintenances saved = maintenanceRepository.save(maintenance);
        machineStatusService.refresh(maintenance.getMachine().getMachineId());
        return saved;
    }

    @Transactional
    public void cancel(Integer maintenanceId, Integer ownerId) {
        Maintenances maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy lịch bảo trì."));
        if (!maintenance.getMachine().getOwner().getUserId().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không sở hữu máy này.");
        }
        if (!"SCHEDULED".equals(maintenance.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Chỉ có thể hủy lịch bảo trì chưa bắt đầu.");
        }
        Integer machineId = maintenance.getMachine().getMachineId();
        maintenanceRepository.delete(maintenance);
        machineStatusService.refresh(machineId);
    }

    @Scheduled(fixedDelayString = "${app.maintenance.refresh-ms:60000}")
    @Transactional
    public void advanceStatuses() {
        LocalDateTime now = LocalDateTime.now();
        List<Maintenances> records = maintenanceRepository.findAll();
        List<Maintenances> changed = new ArrayList<>();
        for (Maintenances maintenance : records) {
            if ("COMPLETED".equals(maintenance.getStatus())) {
                continue;
            }
            String nextStatus;
            if (!maintenance.getEndTime().isAfter(now)) {
                nextStatus = "COMPLETED";
            } else if (!maintenance.getStartTime().isAfter(now)) {
                nextStatus = "IN_PROGRESS";
            } else {
                nextStatus = "SCHEDULED";
            }
            if (!nextStatus.equals(maintenance.getStatus())) {
                maintenance.setStatus(nextStatus);
                changed.add(maintenance);
            }
        }
        if (!changed.isEmpty()) {
            maintenanceRepository.saveAll(changed);
        }
        for (Machines machine : machineRepository.findAll()) {
            machineStatusService.refresh(machine.getMachineId());
        }
    }
}
