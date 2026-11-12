package com.agr04.service;

import com.agr04.model.Machines;
import com.agr04.repository.BookingRepository;
import com.agr04.repository.MachineRepository;
import com.agr04.repository.MaintenanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MachineStatusService {
    private final MachineRepository machineRepository;
    private final BookingRepository bookingRepository;
    private final MaintenanceRepository maintenanceRepository;

    public MachineStatusService(MachineRepository machineRepository,
                                BookingRepository bookingRepository,
                                MaintenanceRepository maintenanceRepository) {
        this.machineRepository = machineRepository;
        this.bookingRepository = bookingRepository;
        this.maintenanceRepository = maintenanceRepository;
    }

    @Transactional
    public void refresh(Integer machineId) {
        Machines machine = machineRepository.findById(machineId).orElse(null);
        if (machine == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        String status;
        if (maintenanceRepository.countCurrentMaintenances(machineId, now) > 0) {
            status = "IN_MAINTENANCE";
        } else if (bookingRepository.countCurrentBookings(machineId, now) > 0) {
            status = "RESERVED";
        } else {
            status = "AVAILABLE";
        }
        if (!status.equals(machine.getStatus())) {
            machine.setStatus(status);
            machineRepository.save(machine);
        }
    }
}
