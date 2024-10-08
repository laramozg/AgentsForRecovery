package org.example.sports.mapper;

import org.example.sports.controller.mutilation.dto.MutilationDto;
import org.example.sports.controller.order.dto.CreateOrderRequest;
import org.example.sports.controller.order.dto.OrderDto;
import org.example.sports.model.Mutilation;
import org.example.sports.model.Order;
import org.example.sports.model.enums.OrderStatus;
import org.example.sports.service.CityService;
import org.example.sports.service.MutilationService;
import org.example.sports.service.UserService;
import org.example.sports.service.VictimService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import static org.example.sports.util.Models.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class OrderMapperTest {
    @Mock
    private UserService userService;

    @Mock
    private CityService cityService;

    @Mock
    private VictimService victimService;

    @Mock
    private MutilationService mutilationService;

    @InjectMocks
    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConvertToDto() {
        Order order = ORDER();

        OrderDto orderDto = orderMapper.convertToDto(order);

        assertEquals(order.getId(), orderDto.id());
        assertEquals(order.getUser().getUsername(), orderDto.username());
        assertEquals(order.getCity().getId(), orderDto.cityId());
        assertEquals(order.getVictim().getId(), orderDto.victimId());
        assertEquals(order.getMutilations().size(), orderDto.mutilations().size());

        MutilationDto mutilationDto = orderDto.mutilations().iterator().next();
        Mutilation mutilation = order.getMutilations().iterator().next();

        assertEquals(mutilation.getId(), mutilationDto.id());
        assertEquals(mutilation.getType(), mutilationDto.type());
        assertEquals(mutilation.getPrice(), mutilationDto.price());
    }

    @Test
    void testConvertToDtoMutilationNull() {
        Order order = Order.builder().id(1L).user(USER_CUSTOMER())
                .city(CITY()).victim(VICTIM()).deadline(LocalDate.now())
                .status(OrderStatus.WAITING).mutilations(null).build();

        OrderDto orderDto = orderMapper.convertToDto(order);
        assertEquals(Collections.emptySet(), orderDto.mutilations());
    }

    @Test
    void testConvert() {
        CreateOrderRequest request = CREATE_ORDER_REQUEST();

        when(userService.getUserById(request.username())).thenReturn(USER_CUSTOMER());
        when(cityService.getCityById(request.cityId())).thenReturn(CITY());
        when(victimService.getVictimById(request.victimId())).thenReturn(VICTIM());
        when(mutilationService.findAllMutilationsById(request.mutilationIds())).thenReturn(Set.of(MUTILATION()));

        Order order = orderMapper.convert(request);

        assertEquals(request.username(), order.getUser().getUsername());
        assertEquals(request.cityId(), order.getCity().getId());
        assertEquals(request.victimId(), order.getVictim().getId());
        assertEquals(request.mutilationIds().size(), order.getMutilations().size());
    }
}
