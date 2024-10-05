package org.example.sports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sports.controller.mutilation.dto.CreateMutilation;
import org.example.sports.controller.mutilation.dto.MutilationDto;
import org.example.sports.service.MutilationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
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

    private CreateMutilation createMutilation;
    private MutilationDto mutilationDto;

    @BeforeEach
    void setUp() {
        createMutilation = new CreateMutilation("description", 10000);
        mutilationDto = new MutilationDto(1L, "description", 10000);
    }

    @Test
    void testCreateMutilation() throws Exception {
        Mockito.when(mutilationService.createMutilation(any(CreateMutilation.class))).thenReturn(mutilationDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/sports/user/mutilation/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMutilation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type", is("description")))
                .andExpect(jsonPath("$.price", is(10000)));
    }

    @Test
    void testGetAllMutilations() throws Exception {
        List<MutilationDto> mutilationDtos = Collections.singletonList(mutilationDto);

        Mockito.when(mutilationService.findAllMutilations(anyInt(), anyInt())).thenReturn(mutilationDtos);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/sports/user/mutilation")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type", is("description")))
                .andExpect(jsonPath("$[0].price", is(10000)));
    }

    @Test
    void testGetMutilationById() throws Exception {
        Mockito.when(mutilationService.findMutilationById(1L)).thenReturn(mutilationDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/sports/user/mutilation/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("description")))
                .andExpect(jsonPath("$.price", is(10000)));
    }

    @Test
    void testUpdateMutilation() throws Exception {
        Mockito.when(mutilationService.updateMutilation(eq(1L), any(CreateMutilation.class))).thenReturn(mutilationDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/sports/user/mutilation/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMutilation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("description")))
                .andExpect(jsonPath("$.price", is(10000)));
    }
}
