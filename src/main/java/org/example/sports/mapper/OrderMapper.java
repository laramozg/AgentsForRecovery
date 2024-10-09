package org.example.sports.mapper;

import lombok.AllArgsConstructor;
import org.example.sports.controller.mutilation.dto.MutilationDto;
import org.example.sports.controller.order.dto.CreateOrderRequest;
import org.example.sports.controller.order.dto.OrderDto;
import org.example.sports.model.Mutilation;
import org.example.sports.model.Order;
import org.example.sports.model.OrderMutilation;
import org.example.sports.service.CityService;
import org.example.sports.service.MutilationService;
import org.example.sports.service.UserService;
import org.example.sports.service.VictimService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Component
public class OrderMapper {
    private final UserService userService;
    private final CityService cityService;
    private final VictimService victimService;
    private final MutilationService mutilationService;

    public OrderDto convertToDto(Order order) {
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
                                .toList() :
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

    public Order convert(CreateOrderRequest createOrderRequest){
        List<Mutilation> mutilations = (mutilationService.findAllMutilationsById(createOrderRequest.mutilationIds()));
        Order order = Order.builder()
                .user(userService.getUserById(createOrderRequest.username()))
                .city(cityService.getCityById(createOrderRequest.cityId()))
                .victim(victimService.getVictimById(createOrderRequest.victimId()))
                .deadline(createOrderRequest.deadline())
                .build();

        List<OrderMutilation> orderMutilations = mutilations.stream()
                .map(mutilation -> new OrderMutilation(order, mutilation))
                .toList();
        order.setOrderMutilations(orderMutilations);
        return order;
    }


}
