package com.agr04.repository;

import com.agr04.model.Maintenances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenances, Integer> {
    @Query("SELECT COUNT(m) FROM Maintenances m WHERE m.machine.machineId = :machineId " +
           "AND m.status IN ('SCHEDULED', 'IN_PROGRESS') " +
           "AND (:startTime < m.endTime AND :endTime > m.startTime)")
    long countOverlappingMaintenances(@Param("machineId") Integer machineId,
                                      @Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime);

    @Query("SELECT COUNT(m) FROM Maintenances m WHERE m.machine.machineId = :machineId " +
           "AND m.status IN ('SCHEDULED', 'IN_PROGRESS') AND m.startTime <= :now AND m.endTime > :now")
    long countCurrentMaintenances(@Param("machineId") Integer machineId,
                                  @Param("now") LocalDateTime now);

    List<Maintenances> findByMachine_Owner_UserIdOrderByStartTimeAsc(Integer userId);
    List<Maintenances> findByStatusInAndEndTimeBefore(List<String> statuses, LocalDateTime time);
    List<Maintenances> findByStatusAndStartTimeLessThanEqualAndEndTimeGreaterThan(String status,
                                                                                   LocalDateTime start,
                                                                                   LocalDateTime end);
}
