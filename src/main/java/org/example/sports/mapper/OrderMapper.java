package org.example.sports.mapper;

import org.example.sports.controller.mutilation.dto.MutilationDto;
import org.example.sports.controller.order.dto.OrderDto;
import org.example.sports.model.Mutilation;
import org.example.sports.model.Order;
import org.example.sports.model.OrderMutilation;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class OrderMapper {
    public OrderDto toDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getUser().getUsername(),
                order.getCity().getId(),
                order.getVictim().getId(),
                order.getDeadline(),
                order.getStatus().toString(),
                order.getOrderMutilations() != null ?
                        order.getOrderMutilations().stream()
                                .map(this::toMutilationDto)
                                .collect(Collectors.toList()) :
                        Collections.emptyList()
        );
    }


    private MutilationDto toMutilationDto(OrderMutilation orderMutilation) {
        Mutilation mutilation = orderMutilation.getMutilation();
        return new MutilationDto(
                mutilation.getId(),
                mutilation.getType(),
                mutilation.getPrice()
        );
    }

}
