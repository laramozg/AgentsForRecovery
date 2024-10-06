package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.sports.controller.order.dto.CreateOrderRequest;
import org.example.sports.controller.order.dto.OrderDto;
import org.example.sports.model.*;
import org.example.sports.model.enums.OrderStatus;
import org.example.sports.model.enums.Role;
import org.example.sports.repositore.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class OrderServiceTest extends AbstractServiceTest{

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private VictimRepository victimRepository;

    @Autowired
    private MutilationRepository mutilationRepository;

    @Autowired
    private OrderRepository orderRepository;

    private User userCreate;
    private City cityCreate;
    private Victim victimCreate;

    @BeforeEach
    void setUp() {
        userCreate = buildCreateUser();
        cityCreate = buildCreateCity();
        victimCreate = buildCreateVictim();
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        userRepository.deleteAll();
        cityRepository.deleteAll();
        victimRepository.deleteAll();
        mutilationRepository.deleteAll();
    }

    @Test
    void testCreateOrder() {
        Mutilation mutilationCreate = buildCreateMutilation();

        CreateOrderRequest request = new CreateOrderRequest(
                userCreate.getUsername(),
                cityCreate.getId(),
                victimCreate.getId(),
                LocalDate.now().plusDays(5),
                List.of(mutilationCreate.getId())
        );

        OrderDto createdOrder = orderService.createOrder(request);

        assertNotNull(createdOrder);
        assertEquals(OrderStatus.WAITING.toString(), createdOrder.state());
        assertEquals(1, createdOrder.mutilations().size());
        assertEquals(mutilationCreate.getId(), createdOrder.mutilations().get(0).id());
    }

    @Test
    void testGetOrdersWithStatusWait() {
        buildCreateOrder(userCreate,cityCreate,victimCreate,OrderStatus.WAITING,LocalDate.now().plusDays(5));
        buildCreateOrder(userCreate,cityCreate,victimCreate,OrderStatus.DONE,LocalDate.now().plusDays(5));

        Page<OrderDto> waitingOrders = orderService.getOrdersWithStatusWait(0, 10);

        assertNotNull(waitingOrders);
        assertEquals(1, waitingOrders.getTotalElements());
        assertEquals(OrderStatus.WAITING.toString(), waitingOrders.getContent().get(0).state());
    }

    @Test
    void testGetOrdersByUsername() {
        buildCreateOrder(userCreate,cityCreate,victimCreate,OrderStatus.WAITING,LocalDate.now().plusDays(5));

        Page<OrderDto> userOrders = orderService.getOrdersByUsername(userCreate.getUsername(), 0, 10);

        assertNotNull(userOrders);
        assertEquals(1, userOrders.getTotalElements());
        assertEquals(userCreate.getUsername(), userOrders.getContent().get(0).username());
    }

    @Test
    void testDeleteOrder() {
        Order order = buildCreateOrder(userCreate,cityCreate,victimCreate,OrderStatus.WAITING,LocalDate.now().plusDays(5));

        orderService.deleteOrder(order.getId());

        assertThrows(EntityNotFoundException.class, () -> orderRepository.findById(order.getId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found")));
    }

    private User buildCreateUser() {
        return userRepository.save(User.builder().username("testUser").nick("user").telegram("@user").role(Role.CUSTOMER).build());
    }

    private City buildCreateCity() {
        return cityRepository.save(City.builder().name("TestCity").region("Region").build());
    }

    private Victim buildCreateVictim() {
        return victimRepository.save(Victim.builder().firstName("John").lastName("Doe").workplace("Workplace")
                .position("Position").residence("Address").phone("1234567890").description("Description").build());
    }

    private Order buildCreateOrder(User user, City city, Victim victim, OrderStatus status, LocalDate deadline){
        return orderRepository.save(Order.builder().user(user).city(city).victim(victim)
                .status(status).deadline(deadline).build());
    }
    private Mutilation buildCreateMutilation() {
        return mutilationRepository.save( Mutilation.builder().type("Type A").price(1000).build());
    }

}