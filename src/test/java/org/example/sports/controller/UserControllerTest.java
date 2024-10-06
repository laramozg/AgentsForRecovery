package org.example.sports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sports.controller.user.dto.CreateUserRequest;
import org.example.sports.controller.user.dto.UpdateUserRequest;
import org.example.sports.controller.user.dto.UserDto;
import org.example.sports.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    private final CreateUserRequest createUserRequest = new CreateUserRequest("user1","user1",
            "@user1", "password123","EXECUTOR");
    private final UserDto userDto = new UserDto("user1", "user1", "@user1","EXECUTOR");
    private final UpdateUserRequest updateUserRequest = new UpdateUserRequest("update_user1",
            "@update_user1");


    @Test
    void testCreateUser() throws Exception {
        Mockito.when(userService.createUser(any(CreateUserRequest.class))).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/sports/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is("user1")))
                .andExpect(jsonPath("$.nick", is("user1")))
                .andExpect(jsonPath("$.telegram", is("@user1")))
                .andExpect(jsonPath("$.role", is("EXECUTOR")));
    }


    @Test
    void testGetUser() throws Exception {
        Mockito.when(userService.getUser("user1")).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/sports/user/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("user1")))
                .andExpect(jsonPath("$.nick", is("user1")));
    }

    @Test
    void testDeleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser("user1");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/sports/user/john_doe"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateUser() throws Exception {
        UserDto userDtoUpdate = new UserDto("user1", "update_user1", "@update_user1","EXECUTOR");
        Mockito.when(userService.updateUser(eq("user1"), any(UpdateUserRequest.class))).thenReturn(userDtoUpdate);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/sports/user/user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nick", is("update_user1")))
                .andExpect(jsonPath("$.telegram", is("@update_user1")));
    }

}
