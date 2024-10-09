package org.example.sports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.sports.BaseIntegrationTest;
import org.example.sports.controller.user.dto.CreateUserRequest;
import org.example.sports.controller.user.dto.UpdateUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.example.sports.util.Models.CREATE_USER_REQUEST;
import static org.example.sports.util.Models.UPDATE_USER_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class UserControllerTest extends BaseIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    private final CreateUserRequest createUserRequest = CREATE_USER_REQUEST();


    @Test
    void testCreateUserSuccess() throws Exception {
        CreateUserRequest user = new CreateUserRequest("user1", "user1", "@user", "user_password1", "EXECUTOR");
        mockMvc.perform(post(API_PREFIX + "user")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(user.username()))
                .andExpect(jsonPath("$.nick").value(user.nick()))
                .andExpect(jsonPath("$.telegram").value(user.telegram()));
    }

    @Test
    void testGetUserByUsernameSuccess() throws Exception {
        mockMvc.perform(get(API_PREFIX + "user/{username}", createUserRequest.username()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(createUserRequest.username()))
                .andExpect(jsonPath("$.nick").value(createUserRequest.nick()));
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        mockMvc.perform(delete(API_PREFIX + "user/{username}", createUserRequest.username()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        UpdateUserRequest updateUserRequest = UPDATE_USER_REQUEST();

        mockMvc.perform(put(API_PREFIX + "user/{username}", createUserRequest.username())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.telegram").value(updateUserRequest.telegram()))
                .andExpect(jsonPath("$.nick").value(updateUserRequest.nick()));
        ;
    }
}

