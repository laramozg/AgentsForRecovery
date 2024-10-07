package org.example.sports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sports.controller.mutilation.dto.MutilationRequest;
import org.example.sports.model.Mutilation;
import org.example.sports.service.MutilationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MutilationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MutilationService mutilationService;

    private MutilationRequest mutilationRequest;
    private Mutilation mutilation;

    @BeforeEach
    void setUp() {
        mutilationRequest = new MutilationRequest("description", 10000);
        mutilation = new Mutilation(1L, "description", 10000, null);
    }

    @Test
    void testCreateMutilationSuccess() throws Exception {
        when(mutilationService.createMutilation(any(MutilationRequest.class))).thenReturn(mutilation);

        mockMvc.perform(post("/api/v1/sports/user/mutilation/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mutilationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type", is("description")))
                .andExpect(jsonPath("$.price", is(10000)));
    }

    @Test
    void testGetAllMutilationsSuccess() throws Exception {
        Page<Mutilation> mutilationDtos = new PageImpl<>(Collections.singletonList(mutilation));

        when(mutilationService.findAllMutilations(anyInt(), anyInt())).thenReturn(mutilationDtos);

        mockMvc.perform(get("/api/v1/sports/user/mutilation")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type", is("description")))
                .andExpect(jsonPath("$[0].price", is(10000)));
    }

    @Test
    void testGetMutilationByIdSuccess() throws Exception {
        when(mutilationService.findMutilationById(1L)).thenReturn(mutilation);

        mockMvc.perform(get("/api/v1/sports/user/mutilation/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("description")))
                .andExpect(jsonPath("$.price", is(10000)));
    }

    @Test
    void testUpdateMutilationSuccess() throws Exception {
        when(mutilationService.updateMutilation(eq(1L), any(MutilationRequest.class))).thenReturn(mutilation);

        mockMvc.perform(put("/api/v1/sports/user/mutilation/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mutilationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("description")))
                .andExpect(jsonPath("$.price", is(10000)));
    }

    @Test
    void testDeleteMutilationSuccess() throws Exception {
        doNothing().when(mutilationService).deleteMutilation(1L);

        mockMvc.perform(delete("/api/v1/sports/user/mutilation/change/1"))
                .andExpect(status().isNoContent());

        verify(mutilationService, times(1)).deleteMutilation(1L);
    }
}
