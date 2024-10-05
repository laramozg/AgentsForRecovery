package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.sports.controller.fight.dto.CreateFight;
import org.example.sports.controller.fight.dto.FightDto;
import org.example.sports.mapper.FightMapper;
import org.example.sports.model.Executor;
import org.example.sports.model.Fight;
import org.example.sports.model.Order;
import org.example.sports.model.User;
import org.example.sports.model.enums.FightStatus;
import org.example.sports.model.enums.OrderStatus;
import org.example.sports.model.enums.Role;
import org.example.sports.repositore.ExecutorRepository;
import org.example.sports.repositore.FightRepository;
import org.example.sports.repositore.OrderRepository;
import org.example.sports.repositore.UserRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class FightServiceTest {

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
    private FightService fightService;

    @Autowired
    private FightRepository fightRepository;

    @Autowired
    private ExecutorRepository executorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FightMapper fightMapper;

    @BeforeEach
    void setUp() {
        fightRepository.deleteAll();
        orderRepository.deleteAll();
        executorRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testGetFightsByExecutorId() {
        User user = userRepository.save(new User("testUser", "user", "@user", Role.EXECUTOR, null));
        Executor executor = executorRepository.save(new Executor("testUser", "12345", 55.5, 155.5, 0.0, 0, user));
        Order order = orderRepository.save(new Order(1L, null, null, null, LocalDate.now(), OrderStatus.WAITING, null));
        Fight fight = fightRepository.save(Fight.builder()
                .executor(executor)
                .order(order)
                .status(FightStatus.PENDING)
                .build());

        Page<FightDto> fights = fightService.getFightsByExecutorId(executor.getUsername(), 0, 10);

        assertNotNull(fights);
        assertEquals(1, fights.getTotalElements());
        assertEquals(fight.getId(), fights.getContent().get(0).id());
    }

    @Test
    void testCreateFight() {
        User user = userRepository.save(new User("testUser", "user", "@user", Role.EXECUTOR, null));
        Executor executor = executorRepository.save(new Executor("testUser", "12345", 55.5, 155.5, 0.0, 0, user));
        Order order = orderRepository.save(new Order(1L, null, null, null, LocalDate.now(), OrderStatus.WAITING, null));

        CreateFight createFight = new CreateFight(executor.getUsername(), order.getId());

        FightDto createdFight = fightService.createFight(createFight);

        assertNotNull(createdFight);
        assertEquals(FightStatus.PENDING.toString(), createdFight.status());
        assertEquals(executor.getUsername(), createdFight.executorId());
        assertEquals(order.getId(), createdFight.orderId());
    }

    @Test
    void testUpdateFightStatus() {
        User user = userRepository.save(new User("testUser", "user", "@user", Role.EXECUTOR, null));
        Executor executor = executorRepository.save(new Executor("testUser", "12345", 55.5, 155.5, 0.0, 0, user));
        Order order = orderRepository.save(new Order(1L, null, null, null, LocalDate.now(), OrderStatus.WAITING, null));
        Fight fight = fightRepository.save(Fight.builder()
                .executor(executor)
                .order(order)
                .status(FightStatus.PENDING)
                .build());

        FightDto updatedFight = fightService.updateFightStatus(fight.getId(), FightStatus.VICTORY);

        assertNotNull(updatedFight);
        assertEquals(FightStatus.VICTORY.toString(), updatedFight.status());

        Order updatedOrder = orderRepository.findById(order.getId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        assertEquals(OrderStatus.DONE, updatedOrder.getStatus());

        Executor updatedExecutor = executorRepository.findById(executor.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Executor not found"));
        assertEquals(1, updatedExecutor.getCompletedOrders());

        assertTrue(updatedExecutor.getRating() > 0);
    }

    @Test
    void testUpdateExecutorRating() {
        User user = userRepository.save(new User("testUser", "user", "@user", Role.EXECUTOR, null));
        Executor executor = executorRepository.save(new Executor("testUser", "12345", 55.5, 155.5, 0.0, 0, user));
        Order order = orderRepository.save(new Order(1L, user, null, null, LocalDate.now(), OrderStatus.WAITING, null));
        Fight fight = fightRepository.save(Fight.builder()
                .executor(executor)
                .order(order)
                .status(FightStatus.VICTORY)
                .build());

        fightService.updateFightStatus(fight.getId(), FightStatus.VICTORY);

        Executor updatedExecutor = executorRepository.findById(executor.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Executor not found"));
        assertEquals(10.0, updatedExecutor.getRating());
    }

    @Test
    void testCreateFightThrowsExceptionIfExecutorNotFound() {
        Order order = orderRepository.save(new Order(1L, null, null, null, LocalDate.now(), OrderStatus.WAITING, null));

        CreateFight createFight = new CreateFight("invalidExecutor", order.getId());

        assertThrows(EntityNotFoundException.class, () -> fightService.createFight(createFight));
    }

    @Test
    void testCreateFightThrowsExceptionIfOrderNotFound() {
        Executor executor = executorRepository.save(new Executor("testExecutor", "12345", 55.5, 155.5, 0.0, 0, null));

        CreateFight createFight = new CreateFight(executor.getUsername(), 999L);

        assertThrows(EntityNotFoundException.class, () -> fightService.createFight(createFight));
    }
}
