package com.agr04.repository;

import com.agr04.model.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Bookings, Integer> {
    
    @Query("SELECT COUNT(b) FROM Bookings b WHERE b.machine.machineId = :machineId " +
           "AND b.status IN ('PENDING', 'CONFIRMED', 'IN_PROGRESS') " +
           "AND (:startTime < b.endTime AND :endTime > b.startTime)")
    long countOverlappingBookings(@Param("machineId") Integer machineId,
                                  @Param("startTime") LocalDateTime startTime,
                                  @Param("endTime") LocalDateTime endTime);

    @Query("SELECT COUNT(b) FROM Bookings b WHERE b.operator.operatorId = :operatorId " +
           "AND b.status IN ('PENDING', 'CONFIRMED', 'IN_PROGRESS') " +
           "AND (:startTime < b.endTime AND :endTime > b.startTime)")
    long countOverlappingOperatorBookings(@Param("operatorId") Integer operatorId,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    @Query("SELECT COUNT(b) FROM Bookings b WHERE b.machine.machineId = :machineId " +
           "AND b.status IN ('PENDING', 'CONFIRMED', 'IN_PROGRESS') " +
           "AND (:startTime < b.endTime AND :endTime > b.startTime)")
    long countActiveBookingsOverlapping(@Param("machineId") Integer machineId,
                                        @Param("startTime") LocalDateTime startTime,
                                        @Param("endTime") LocalDateTime endTime);

    @Query("SELECT COUNT(b) FROM Bookings b WHERE b.machine.machineId = :machineId " +
           "AND b.status IN ('PENDING', 'CONFIRMED', 'IN_PROGRESS') " +
           "AND b.startTime <= :now AND b.endTime > :now")
    long countCurrentBookings(@Param("machineId") Integer machineId,
                              @Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE Bookings b SET b.status = :status WHERE b.bookingId = :bookingId")
    int updateStatus(@Param("bookingId") Integer bookingId, @Param("status") String status);

    List<Bookings> findByFarmer_UserIdOrderByStartTimeDesc(Integer userId);

    List<Bookings> findByMachine_Owner_UserIdOrderByStartTimeDesc(Integer userId);

    List<Bookings> findByOperator_User_UserIdOrderByStartTimeDesc(Integer userId);
}