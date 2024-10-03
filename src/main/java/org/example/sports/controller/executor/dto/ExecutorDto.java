package org.example.sports.controller.executor.dto;

public record ExecutorDto(
        String username,
        String passportSeriesNumber,
        Double weight,
        Double height,
        Double rating,
        Integer completedOrders
) {}