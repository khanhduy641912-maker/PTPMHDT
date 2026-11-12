package com.agr04.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Operators")
public class Operators {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operator_id")
    private Integer operatorId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private Users user;

    @Column(name = "license_no", nullable = false, length = 50)
    private String licenseNo;

    @Column(name = "is_eligible")
    private Boolean isEligible = true;

    @Column(name = "hourly_rate")
    private BigDecimal hourlyRate;

    public Operators() {}

    // Getters and Setters
    public Integer getOperatorId() { return operatorId; }
    public void setOperatorId(Integer operatorId) { this.operatorId = operatorId; }

    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }

    public String getLicenseNo() { return licenseNo; }
    public void setLicenseNo(String licenseNo) { this.licenseNo = licenseNo; }

    public Boolean getIsEligible() { return isEligible; }
    public void setIsEligible(Boolean isEligible) { this.isEligible = isEligible; }

    public BigDecimal getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }
}