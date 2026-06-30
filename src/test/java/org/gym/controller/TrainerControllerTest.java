package org.gym.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.gym.dto.RegistrationResponse;
import org.gym.dto.TrainerProfileResponse;
import org.gym.dto.TrainingTypeResponse;
import org.gym.exception.GlobalExceptionHandler;
import org.gym.exception.ResourceNotFoundException;
import org.gym.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainerController trainerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc = MockMvcBuilders.standaloneSetup(trainerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void registerReturnsCredentials() throws Exception {
        when(trainerService.register(any())).thenReturn(new RegistrationResponse("amy.lee1", "pass123456"));

        mockMvc.perform(post("/api/trainers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"amy\",\"lastName\":\"lee\",\"specializationId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("amy.lee1"));
    }

    @Test
    void registerRejectsMissingSpecialization() throws Exception {
        mockMvc.perform(post("/api/trainers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"amy\",\"lastName\":\"lee\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void registerReturnsNotFoundForUnknownType() throws Exception {
        when(trainerService.register(any()))
                .thenThrow(new ResourceNotFoundException("Training type not found: 99"));

        mockMvc.perform(post("/api/trainers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"amy\",\"lastName\":\"lee\",\"specializationId\":99}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void getProfileReturnsProfile() throws Exception {
        TrainerProfileResponse profile = new TrainerProfileResponse(
                "amy.lee1", "amy", "lee", new TrainingTypeResponse(1L, "POWER-BASED"), true, List.of());
        when(trainerService.getProfile("amy.lee1")).thenReturn(profile);

        mockMvc.perform(get("/api/trainers/amy.lee1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.specialization.name").value("POWER-BASED"));
    }
}
