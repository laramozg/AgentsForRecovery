package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.sports.controller.order.dto.CreateOrderRequest;
import org.example.sports.controller.order.dto.OrderDto;
import org.example.sports.mapper.OrderMapper;
import org.example.sports.model.*;
import org.example.sports.model.enums.OrderStatus;
import org.example.sports.repositore.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMutilationRepository orderMutilationRepository;
    private final MutilationRepository mutilationRepository;
    private final UserRepository userRepository;
    private final VictimRepository victimRepository;
    private final CityRepository cityRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderDto createOrder(CreateOrderRequest request) {
        User user = userRepository.findById(request.username())
                .orElseThrow(() -> new EntityNotFoundException("City not found"));
        ;
        City city = cityRepository.findById(request.cityId())
                .orElseThrow(() -> new EntityNotFoundException("City not found"));
        Victim victim = victimRepository.findById(request.victimId())
                .orElseThrow(() -> new EntityNotFoundException("Victim not found"));

        Order order = Order.builder()
                .user(user)
                .city(city)
                .victim(victim)
                .deadline(request.deadline())
                .status(OrderStatus.WAITING)
                .build();

        Order savedOrder = orderRepository.save(order);

        for (Long mutilationId : request.mutilationIds()) {
            Mutilation mutilation = mutilationRepository.findById(mutilationId)
                    .orElseThrow(() -> new EntityNotFoundException("Mutilation not found"));

            OrderMutilation orderMutilation = OrderMutilation.builder()
                    .order(savedOrder)
                    .mutilation(mutilation)
                    .build();

            orderMutilationRepository.save(orderMutilation);

        }
        List<OrderMutilation> orderMutilations = orderMutilationRepository.findByOrder_Id(savedOrder.getId());
        savedOrder.setOrderMutilations(orderMutilations);


        return orderMapper.toDto(savedOrder);
    }

    public Page<OrderDto> getOrdersWithStatusWait(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findAllByStatus(OrderStatus.WAITING, pageable);
        return orders.map(orderMapper::toDto);
    }

    public Page<OrderDto> getOrdersByUsername(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findByUser_Username(username, pageable);
        return orders.map(orderMapper::toDto);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

}
