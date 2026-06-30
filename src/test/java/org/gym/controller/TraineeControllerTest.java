package org.gym.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.gym.dto.RegistrationResponse;
import org.gym.dto.TraineeProfileResponse;
import org.gym.exception.GlobalExceptionHandler;
import org.gym.exception.ResourceNotFoundException;
import org.gym.service.TraineeService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

    @Mock
    private TraineeService traineeService;

    @InjectMocks
    private TraineeController traineeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc = MockMvcBuilders.standaloneSetup(traineeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void registerReturnsCredentials() throws Exception {
        when(traineeService.register(any())).thenReturn(new RegistrationResponse("john.doe1", "secret12345"));

        mockMvc.perform(post("/api/trainees/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"john\",\"lastName\":\"doe\",\"dateOfBirth\":\"2000-01-01\",\"address\":\"street\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john.doe1"))
                .andExpect(jsonPath("$.password").value("secret12345"));
    }

    @Test
    void registerRejectsBlankFirstName() throws Exception {
        mockMvc.perform(post("/api/trainees/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"\",\"lastName\":\"doe\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void getProfileReturnsProfile() throws Exception {
        TraineeProfileResponse profile =
                new TraineeProfileResponse("john.doe1", "john", "doe", null, "street", true, List.of());
        when(traineeService.getProfile("john.doe1")).thenReturn(profile);

        mockMvc.perform(get("/api/trainees/john.doe1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("john"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void getProfileReturnsNotFound() throws Exception {
        when(traineeService.getProfile("missing"))
                .thenThrow(new ResourceNotFoundException("Trainee not found: missing"));

        mockMvc.perform(get("/api/trainees/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deleteReturnsOk() throws Exception {
        mockMvc.perform(delete("/api/trainees/john.doe1"))
                .andExpect(status().isOk());
        verify(traineeService).delete("john.doe1");
    }

    @Test
    void patchStatusReturnsOk() throws Exception {
        mockMvc.perform(patch("/api/trainees/john.doe1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"active\":false}"))
                .andExpect(status().isOk());
        verify(traineeService).setActiveStatus(eq("john.doe1"), eq(false));
    }
}
