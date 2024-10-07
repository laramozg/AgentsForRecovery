package org.example.sports.controller.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sports.controller.order.dto.CreateOrderRequest;
import org.example.sports.controller.order.dto.OrderDto;
import org.example.sports.mapper.OrderMapper;
import org.example.sports.model.Order;
import org.example.sports.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sports/user/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Order order = orderService.createOrder(orderMapper.convert(request));
        return new ResponseEntity<>(orderMapper.convertToDto(order), HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<Page<OrderDto>> getOrdersWithStatusWait(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size
    ) {
        if (size > 50) {size = 50;}
        Page<OrderDto> orders = orderService.getOrdersWithStatusWait(page, size).map(orderMapper::convertToDto);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{username}")
    public ResponseEntity<Page<OrderDto>> getOrdersByUsername(
            @PathVariable String username,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size) {
        if (size > 50) {size = 50;}
        Page<OrderDto> orders = orderService.getOrdersByUsername(username, page, size).map(orderMapper::convertToDto);;
        return ResponseEntity.ok(orders);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

}
