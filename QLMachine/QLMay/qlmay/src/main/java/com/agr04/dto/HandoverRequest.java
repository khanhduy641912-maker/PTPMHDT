package com.agr04.dto;

public record HandoverRequest(
        String type,
        Integer odometer,
        Double fuelPercent,
        String machineCondition,
        String remarks
) {}
