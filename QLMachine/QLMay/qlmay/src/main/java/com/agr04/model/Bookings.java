package com.agr04.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Bookings")
public class Bookings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer bookingId;

    @ManyToOne
    @JoinColumn(name = "farmer_id", nullable = false)
    private Users farmer;

    @ManyToOne
    @JoinColumn(name = "machine_id", nullable = false)
    private Machines machine;

    @ManyToOne
    @JoinColumn(name = "operator_id")
    private Operators operator;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "total_fee", precision = 12, scale = 2)
    private BigDecimal totalFee;

    @Column(length = 20)
    private String status;

    @Column(name = "field_address", length = 255)
    private String fieldAddress;

    public Bookings() {}

    // Getters and Setters
    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }

    public Users getFarmer() { return farmer; }
    public void setFarmer(Users farmer) { this.farmer = farmer; }

    public Machines getMachine() { return machine; }
    public void setMachine(Machines machine) { this.machine = machine; }

    public Operators getOperator() { return operator; }
    public void setOperator(Operators operator) { this.operator = operator; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public BigDecimal getTotalFee() { return totalFee; }
    public void setTotalFee(BigDecimal totalFee) { this.totalFee = totalFee; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getFieldAddress() { return fieldAddress; }
    public void setFieldAddress(String fieldAddress) { this.fieldAddress = fieldAddress; }
}