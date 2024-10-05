package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.sports.controller.order.dto.CreateOrderRequest;
import org.example.sports.controller.order.dto.OrderDto;
import org.example.sports.model.*;
import org.example.sports.model.enums.OrderStatus;
import org.example.sports.model.enums.Role;
import org.example.sports.repositore.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

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

    @Autowired
    private OrderMutilationRepository orderMutilationRepository;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        userRepository.deleteAll();
        cityRepository.deleteAll();
        victimRepository.deleteAll();
        mutilationRepository.deleteAll();
    }

    @Test
    void testCreateOrder() {
        User user = userRepository.save(new User("testUser", "user", "@user", Role.EXECUTOR, null));
        City city = cityRepository.save(new City(1L, "TestCity", "Region"));
        Victim victim = victimRepository.save(new Victim(1L, "John", "Doe", "Workplace", "Position", "Address", "1234567890", "Description"));
        Mutilation mutilation = mutilationRepository.save(new Mutilation(1L,"Type A", 1000));

        CreateOrderRequest request = new CreateOrderRequest(
                user.getUsername(),
                city.getId(),
                victim.getId(),
                LocalDate.now().plusDays(5),
                List.of(mutilation.getId())
        );

        OrderDto createdOrder = orderService.createOrder(request);

        assertNotNull(createdOrder);
        assertEquals(OrderStatus.WAITING.toString(), createdOrder.state());
        assertEquals(1, createdOrder.mutilations().size());
        assertEquals(mutilation.getId(), createdOrder.mutilations().get(0).id());
    }

    @Test
    void testGetOrdersWithStatusWait() {
        User user = userRepository.save(new User("testUser", "user", "@user", Role.EXECUTOR, null));
        City city = cityRepository.save(new City(1L, "TestCity", "Region"));
        Victim victim = victimRepository.save(new Victim(1L, "John", "Doe", "Workplace", "Position", "Address", "1234567890", "Description"));

        Order order1 = Order.builder().user(user).city(city).victim(victim).status(OrderStatus.WAITING).deadline(LocalDate.now()).build();
        Order order2 = Order.builder().user(user).city(city).victim(victim).status(OrderStatus.DONE).deadline(LocalDate.now()).build();
        orderRepository.saveAll(List.of(order1, order2));

        Page<OrderDto> waitingOrders = orderService.getOrdersWithStatusWait(0, 10);

        assertNotNull(waitingOrders);
        assertEquals(1, waitingOrders.getTotalElements());
        assertEquals(OrderStatus.WAITING.toString(), waitingOrders.getContent().get(0).state());
    }

    @Test
    void testGetOrdersByUsername() {
        User user = userRepository.save(new User("testUser", "user", "@user", Role.EXECUTOR, null));
        City city = cityRepository.save(new City(1L, "TestCity", "Region"));
        Victim victim = victimRepository.save(new Victim(1L, "John", "Doe", "Workplace", "Position", "Address", "1234567890", "Description"));

        Order order = Order.builder().user(user).city(city).victim(victim).status(OrderStatus.WAITING).deadline(LocalDate.now()).build();
        orderRepository.save(order);

        Page<OrderDto> userOrders = orderService.getOrdersByUsername(user.getUsername(), 0, 10);

        assertNotNull(userOrders);
        assertEquals(1, userOrders.getTotalElements());
        assertEquals(user.getUsername(), userOrders.getContent().get(0).username());
    }

    @Test
    void testDeleteOrder() {
        User user = userRepository.save(new User("testUser", "user", "@user", Role.EXECUTOR, null));
        City city = cityRepository.save(new City(1L, "TestCity", "Region"));
        Victim victim = victimRepository.save(new Victim(1L, "John", "Doe", "Workplace", "Position", "Address", "1234567890", "Description"));

        Order order = Order.builder().user(user).city(city).victim(victim).status(OrderStatus.WAITING).deadline(LocalDate.now()).build();
        Order savedOrder = orderRepository.save(order);

        orderService.deleteOrder(savedOrder.getId());

        assertThrows(EntityNotFoundException.class, () -> orderRepository.findById(savedOrder.getId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found")));
    }
}