package com.agr04.repository;

import com.agr04.model.Handovers;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HandoverRepository extends JpaRepository<Handovers, Integer> {
    boolean existsByBooking_BookingIdAndType(Integer bookingId, String type);
    java.util.Optional<Handovers> findByHandoverIdAndBooking_BookingId(Integer handoverId, Integer bookingId);
    List<Handovers> findByBooking_BookingIdOrderByHandoverTimeAsc(Integer bookingId);
}
