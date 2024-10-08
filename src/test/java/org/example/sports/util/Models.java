package org.example.sports.util;

import org.example.sports.controller.city.dto.CreateCity;
import org.example.sports.controller.executor.dto.ExecutorRequest;
import org.example.sports.controller.fight.dto.CreateFight;
import org.example.sports.controller.mutilation.dto.MutilationRequest;
import org.example.sports.controller.order.dto.CreateOrderRequest;
import org.example.sports.controller.user.dto.CreateUserRequest;
import org.example.sports.controller.user.dto.UpdateUserRequest;
import org.example.sports.controller.victim.dto.CreateVictimRequest;
import org.example.sports.model.*;
import org.example.sports.model.enums.FightStatus;
import org.example.sports.model.enums.OrderStatus;
import org.example.sports.model.enums.Role;

import java.time.LocalDate;
import java.util.Set;

public class Models {

    public static String ENCODED_USER_PASSWORD = "$2a$10$.IEUyyxTZjIGYnDHOcFW3e8AD5QFAKWj7nu7NM1NfBs.wE6AtC83a";

    public static CreateUserRequest CREATE_USER_REQUEST() {
        return new CreateUserRequest("john.doe", "johnny", "johnny_telegram",
                "validpassword123", "EXECUTOR");
    }

    public static UpdateUserRequest UPDATE_USER_REQUEST() {
        return new UpdateUserRequest("johnny_updated", "johnny_telegram_updated");
    }

    public static User USER_EXECUTOR() {
        return User.builder()
                .username("john.doe")
                .nick("johnny")
                .telegram("johnny_telegram")
                .role(Role.EXECUTOR)
                .build();
    }

    public static User USER_CUSTOMER() {
        return User.builder()
                .username("john")
                .nick("johnny")
                .telegram("johnny_telegram")
                .role(Role.CUSTOMER)
                .build();
    }

    public static AuthorizationData AUTH_DATA() {
        return AuthorizationData.builder()
                .username("john.doe")
                .password(ENCODED_USER_PASSWORD)
                .build();
    }

    public static Executor EXECUTOR(){
        return Executor.builder()
                .username("john.doe")
                .passportSeriesNumber("123456789")
                .weight(80.0)
                .height(180.0)
                .rating(0.0)
                .completedOrders(0)
                .build();
    }


    public static ExecutorRequest EXECUTOR_REQUEST(){
        return new ExecutorRequest("john.doe", "123456789", 80.0, 180.0);
    }


    public static CreateCity CREATE_CITY_REQUEST(){
        return new CreateCity("New City", "New Region");
    }

    public static City CITY(){
        return City.builder()
                .id(1L)
                .name("New City")
                .region("New Region")
                .build();
    }

    public static CreateVictimRequest CREATE_VICTIM_REQUEST(){
        return new CreateVictimRequest("John", "Doe", "Company", "Manager",
                "New York", "555-1234","Injured");
    }

    public static Victim VICTIM(){
        return Victim.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .workplace("Company")
                .position("Manager")
                .residence("New York")
                .phone("555-1234")
                .description("Injured").build();
    }

    public static MutilationRequest CREATE_MUTILATION_REQUEST(){
        return new MutilationRequest("Type", 1000);
    }

    public static Mutilation MUTILATION(){
        return Mutilation.builder()
                .id(1L)
                .type("Type")
                .price(1000).build();
    }

    public static CreateOrderRequest CREATE_ORDER_REQUEST() {
        return new CreateOrderRequest("john", 1L, 1L, LocalDate.now(), Set.of(1L));

    }

    public static Order ORDER(){
        return Order.builder()
                .id(1L)
                .user(USER_CUSTOMER())
                .city(CITY())
                .victim(VICTIM())
                .deadline(LocalDate.now())
                .status(OrderStatus.WAITING)
                .mutilations(Set.of(MUTILATION())).build();

    }

    public static CreateFight CREATE_FIGHT_REQUEST(){
        return new CreateFight("john.doe", 1L);
    }

    public static Fight FIGHT(){
        return Fight.builder()
                .id(1L)
                .executor(EXECUTOR())
                .order(ORDER())
                .status(FightStatus.PENDING)
                .build();
    }
}
