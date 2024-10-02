package org.example.sports.model.dto;

public record ExecutorDto(
        String username,
        String passportSeriesNumber,
        Double weight,
        Double height,
        Double rating,
        Integer completedOrders
) {}