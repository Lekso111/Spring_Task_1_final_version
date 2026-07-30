package org.gym.controller;

import org.gym.dto.LoginResponse;
import org.gym.exception.AuthenticationException;
import org.gym.exception.GlobalExceptionHandler;
import org.gym.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void loginReturnsTokenForValidCredentials() throws Exception {
        when(loginService.login("john.doe1", "secret12345")).thenReturn(new LoginResponse("a.jwt.token"));

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john.doe1\",\"password\":\"secret12345\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("a.jwt.token"));
        verify(loginService).login("john.doe1", "secret12345");
    }

    @Test
    void loginReturnsUnauthorizedForInvalidCredentials() throws Exception {
        when(loginService.login("john.doe1", "wrong"))
                .thenThrow(new AuthenticationException("Invalid username or password"));

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john.doe1\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void changeLoginReturnsOk() throws Exception {
        mockMvc.perform(put("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john.doe1\",\"oldPassword\":\"oldPass1234\",\"newPassword\":\"newPass5678\"}"))
                .andExpect(status().isOk());
        verify(loginService).changeLogin("john.doe1", "oldPass1234", "newPass5678");
    }
}
