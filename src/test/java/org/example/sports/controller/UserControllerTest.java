package org.example.sports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sports.controller.user.dto.CreateUserRequest;
import org.example.sports.controller.user.dto.UpdateUserRequest;
import org.example.sports.model.User;
import org.example.sports.model.enums.Role;
import org.example.sports.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private CreateUserRequest createUserRequest;
    private User user;
    private UpdateUserRequest updateUserRequest;
    @BeforeEach
    void setUp() {
        createUserRequest = new CreateUserRequest("user1", "user1", "@user1",
                "password123", "EXECUTOR");
        user = new User("user1", "user1", "@user1", Role.EXECUTOR, null);
        updateUserRequest = new UpdateUserRequest("update_user1", "@update_user1");
    }

    @Test
    void testCreateUserSuccess() throws Exception {
        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(user);

        mockMvc.perform(post("/api/v1/sports/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is("user1")))
                .andExpect(jsonPath("$.nick", is("user1")))
                .andExpect(jsonPath("$.telegram", is("@user1")))
                .andExpect(jsonPath("$.role", is("EXECUTOR")));
    }


    @Test
    void testGetUserSuccess() throws Exception {
        when(userService.getUserById("user1")).thenReturn(user);

        mockMvc.perform(get("/api/v1/sports/user/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("user1")))
                .andExpect(jsonPath("$.nick", is("user1")));
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        doNothing().when(userService).deleteUser("user1");

        mockMvc.perform(delete("/api/v1/sports/user/john_doe"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        User userUpdate = new User("user1", "update_user1", "@update_user1", Role.EXECUTOR, null);
        when(userService.updateUser(eq("user1"), any(UpdateUserRequest.class))).thenReturn(userUpdate);

        mockMvc.perform(put("/api/v1/sports/user/user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nick", is("update_user1")))
                .andExpect(jsonPath("$.telegram", is("@update_user1")));
    }

}
