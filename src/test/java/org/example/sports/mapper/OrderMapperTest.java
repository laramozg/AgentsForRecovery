package org.example.sports.mapper;

import org.example.sports.controller.mutilation.dto.MutilationDto;
import org.example.sports.controller.order.dto.CreateOrderRequest;
import org.example.sports.controller.order.dto.OrderDto;
import org.example.sports.model.*;
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
        User user = User.builder().username("testUser").build();
        City city = City.builder().id(1L).build();
        Victim victim = Victim.builder().id(1L).build();
        Mutilation mutilation = Mutilation.builder().id(1L).type("Type").price(1000).build();
        Order order = Order.builder().id(1L).user(user).city(city).victim(victim).deadline(LocalDate.now())
                .status(OrderStatus.WAITING).mutilations(Set.of(mutilation)).build();

        OrderDto orderDto = orderMapper.convertToDto(order);

        assertEquals(1L, orderDto.id());
        assertEquals("testUser", orderDto.username());
        assertEquals(1L, orderDto.cityId());
        assertEquals(1L, orderDto.victimId());
        assertEquals(1, orderDto.mutilations().size());

        MutilationDto mutilationDto = orderDto.mutilations().iterator().next();
        assertEquals(1L, mutilationDto.id());
        assertEquals("Type", mutilationDto.type());
        assertEquals(1000, mutilationDto.price());
    }

    @Test
    void testConvertToDtoMutilationNull() {
        User user = User.builder().username("testUser").build();
        City city = City.builder().id(1L).build();
        Victim victim = Victim.builder().id(1L).build();
        Order order = Order.builder().id(1L).user(user).city(city).victim(victim).deadline(LocalDate.now())
                .status(OrderStatus.WAITING).mutilations(null).build();

        OrderDto orderDto = orderMapper.convertToDto(order);
        assertEquals(Collections.emptySet(), orderDto.mutilations());
    }

    @Test
    void testConvert() {
        CreateOrderRequest request = new CreateOrderRequest("testUser", 1L, 1L, LocalDate.now(),
                Set.of(1L));
        User user = User.builder().username("testUser").build();
        City city = City.builder().id(1L).build();
        Victim victim = Victim.builder().id(1L).build();
        Mutilation mutilation = Mutilation.builder().id(1L).type("Type").price(1000).build();

        when(userService.getUserById("testUser")).thenReturn(user);
        when(cityService.getCityById(1L)).thenReturn(city);
        when(victimService.getVictimById(1L)).thenReturn(victim);
        when(mutilationService.findAllMutilationsById(Set.of(1L))).thenReturn(Set.of(mutilation));

        Order order = orderMapper.convert(request);

        assertEquals(user, order.getUser());
        assertEquals(city, order.getCity());
        assertEquals(victim, order.getVictim());
        assertEquals(1, order.getMutilations().size());
        assertEquals(mutilation, order.getMutilations().iterator().next());
    }
}
