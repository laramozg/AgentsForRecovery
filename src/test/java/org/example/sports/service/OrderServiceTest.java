package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class OrderServiceTest extends AbstractServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ExecutorRepository executorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private VictimRepository victimRepository;

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
        executorRepository.deleteAll();
        userRepository.deleteAll();
        cityRepository.deleteAll();
        victimRepository.deleteAll();
    }

    @Test
    void testCreateOrderSuccess() {
        Order order = Order.builder().user(userCreate).city(cityCreate).victim(victimCreate)
                .deadline(LocalDate.now().plusDays(5)).mutilations(null).build();

        Order createdOrder = orderService.createOrder(order);

        assertNotNull(createdOrder);
        assertEquals(OrderStatus.WAITING, createdOrder.getStatus());

    }

    @Test
    void testGetOrdersWithStatusWaitByIdSuccess() {
        buildCreateOrder(userCreate,cityCreate,victimCreate,OrderStatus.WAITING,LocalDate.now().plusDays(5));
        buildCreateOrder(userCreate,cityCreate,victimCreate,OrderStatus.DONE,LocalDate.now().plusDays(5));

        Page<Order> waitingOrders = orderService.getOrdersWithStatusWait(0, 10);

        assertNotNull(waitingOrders);
        assertEquals(1, waitingOrders.getTotalElements());
        assertEquals(OrderStatus.WAITING, waitingOrders.getContent().get(0).getStatus());
    }

    @Test
    void testGetOrdersByUsernameByIdSuccess() {
        buildCreateOrder(userCreate,cityCreate,victimCreate,OrderStatus.WAITING,LocalDate.now().plusDays(5));

        Page<Order> userOrders = orderService.getOrdersByUsername(userCreate.getUsername(), 0, 10);

        assertNotNull(userOrders);
        assertEquals(1, userOrders.getTotalElements());
        assertEquals(userCreate.getUsername(), userOrders.getContent().get(0).getUser().getUsername());
    }

    @Test
    void testDeleteOrderSuccess() {
        Order order = buildCreateOrder(userCreate,cityCreate,victimCreate,OrderStatus.WAITING,LocalDate.now().plusDays(5));

        orderService.deleteOrder(order.getId());

        assertThrows(EntityNotFoundException.class, () -> orderRepository.findById(order.getId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found")));
    }

    @Test
    void testGetOrderById_NotFound() {
        assertThrows(EntityNotFoundException.class, () -> orderService.getOrderById(999L));
    }


    private Executor buildCreateExecutor(User user) {
        return executorRepository.save(Executor.builder().username(user.getUsername()).passportSeriesNumber("123456")
                .weight(75.0).height(180.0).rating(0.0).completedOrders(0).user(user).build());
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

}