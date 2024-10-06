package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.sports.controller.fight.dto.CreateFight;
import org.example.sports.controller.fight.dto.FightDto;
import org.example.sports.model.*;
import org.example.sports.model.enums.FightStatus;
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
class FightServiceTest extends AbstractServiceTest{

    @Autowired
    private FightService fightService;

    @Autowired
    private FightRepository fightRepository;

    @Autowired
    private ExecutorRepository executorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private VictimRepository victimRepository;

    @Autowired
    private OrderRepository orderRepository;

    private Executor executor;
    private Order order;

    @BeforeEach
    void setUp() {
        User userCustomer = buildCreateUser("testUser2", "user", "@user", Role.CUSTOMER);
        User userExecutor = buildCreateUser("testUser1", "user", "@user", Role.EXECUTOR);
        City city = buildCreateCity();
        Victim victim = buildCreateVictim();
        executor = buildCreateExecutor(userExecutor);
        order = buildCreateOrder(userCustomer, city, victim, OrderStatus.WAITING,LocalDate.now().plusDays(5));

    }

    @AfterEach
    void tearDown() {
        fightRepository.deleteAll();
        orderRepository.deleteAll();
        executorRepository.deleteAll();
        userRepository.deleteAll();
        cityRepository.deleteAll();
        victimRepository.deleteAll();
    }

    @Test
    void testGetFightsByExecutorId() {

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
        CreateFight createFight = new CreateFight(executor.getUsername(), order.getId());

        FightDto createdFight = fightService.createFight(createFight);

        assertNotNull(createdFight);
        assertEquals(FightStatus.PENDING.toString(), createdFight.status());
        assertEquals(executor.getUsername(), createdFight.executorId());
        assertEquals(order.getId(), createdFight.orderId());
    }

    @Test
    void testUpdateFightStatus() {
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
        CreateFight createFight = new CreateFight("invalidExecutor", order.getId());

        assertThrows(EntityNotFoundException.class, () -> fightService.createFight(createFight));
    }


    @Test
    void testUpdateFightStatusThrowsExceptionIfFightNotFound() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            fightService.updateFightStatus(999L, FightStatus.VICTORY);
        });

        assertEquals("Fight not found", exception.getMessage());
    }


    @Test
    void testUpdateFightStatusChangesOrderAndExecutorStatusToWaiting() {
        Fight fight = Fight.builder()
                .executor(executor)
                .order(order)
                .status(FightStatus.PENDING)
                .build();
        fightRepository.save(fight);

        FightDto fightDto = fightService.updateFightStatus(fight.getId(), FightStatus.LOSS);

        assertEquals(OrderStatus.WAITING, order.getStatus());
        assertEquals(FightStatus.LOSS.toString(), fightDto.status());
    }

    private User buildCreateUser(String username, String nick, String telegram, Role role) {
        return userRepository.save(User.builder().username(username).nick(nick).telegram(telegram).role(role).build());
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
    private Executor buildCreateExecutor(User user) {
        return executorRepository.save(Executor.builder().username(user.getUsername()).passportSeriesNumber("123456")
                .weight(75.0).height(180.0).rating(0.0).completedOrders(0).user(user).build());
    }

}
