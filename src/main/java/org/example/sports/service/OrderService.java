package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.sports.model.Order;
import org.example.sports.model.enums.OrderStatus;
import org.example.sports.repositore.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public Order createOrder(Order order) {
        order.setStatus(OrderStatus.WAITING);
        return orderRepository.save(order);
    }

    public Page<Order> getOrdersWithStatusWait(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAllByStatus(OrderStatus.WAITING, pageable);
    }

    public Page<Order> getOrdersByUsername(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findByUser_Username(username, pageable);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
