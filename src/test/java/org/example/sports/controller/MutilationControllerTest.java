package org.example.sports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sports.BaseIntegrationTest;
import org.example.sports.controller.mutilation.dto.MutilationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.example.sports.util.Models.CREATE_MUTILATION_REQUEST;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MutilationControllerTest extends BaseIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    private final MutilationRequest mutilationRequest = CREATE_MUTILATION_REQUEST();

    @Test
    void testCreateMutilationSuccess() throws Exception {
        mockMvc.perform(post(API_PREFIX + "user/mutilation/change")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mutilationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type", is(mutilationRequest.type())))
                .andExpect(jsonPath("$.price", is(mutilationRequest.price())));
    }

//    @Test
//    void testGetAllMutilationsSuccess() throws Exception {
//        mockMvc.perform(get(API_PREFIX + "user/mutilation")
//                        .param("page", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content", is(empty())));
//    }

    @Test
    void testGetMutilationByIdSuccess() throws Exception {
        mockMvc.perform(get(API_PREFIX + "user/mutilation/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateMutilationSuccess() throws Exception {
        mockMvc.perform(put(API_PREFIX + "user/mutilation/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mutilationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is(mutilationRequest.type())))
                .andExpect(jsonPath("$.price", is(mutilationRequest.price())));
    }

    @Test
    void testDeleteMutilationSuccess() throws Exception {
        mockMvc.perform(delete(API_PREFIX + "user/mutilation/change/1"))
                .andExpect(status().isNoContent());

    }
}
