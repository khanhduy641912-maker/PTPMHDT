package com.agr04.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Handovers")
public class Handovers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "handover_id")
    private Integer handoverId;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Bookings booking;

    @Column(name = "handover_time")
    private LocalDateTime handoverTime;

    @Column(name = "machine_condition", nullable = false, columnDefinition = "nvarchar(max)")
    private String machineCondition;

    @Column(length = 20, nullable = false)
    private String type;

    @Column(columnDefinition = "nvarchar(max)")
    private String remarks;

    public Integer getHandoverId() { return handoverId; }
    public void setHandoverId(Integer handoverId) { this.handoverId = handoverId; }
    public Bookings getBooking() { return booking; }
    public void setBooking(Bookings booking) { this.booking = booking; }
    public LocalDateTime getHandoverTime() { return handoverTime; }
    public void setHandoverTime(LocalDateTime handoverTime) { this.handoverTime = handoverTime; }
    public String getMachineCondition() { return machineCondition; }
    public void setMachineCondition(String machineCondition) { this.machineCondition = machineCondition; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
