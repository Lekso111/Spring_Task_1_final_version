package org.gym.controller;

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

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    void loginReturnsOkForValidCredentials() throws Exception {
        mockMvc.perform(get("/api/login")
                        .param("username", "john.doe1")
                        .param("password", "secret12345"))
                .andExpect(status().isOk());
        verify(loginService).login("john.doe1", "secret12345");
    }

    @Test
    void loginReturnsUnauthorizedForInvalidCredentials() throws Exception {
        doThrow(new AuthenticationException("Invalid username or password"))
                .when(loginService).login("john.doe1", "wrong");

        mockMvc.perform(get("/api/login")
                        .param("username", "john.doe1")
                        .param("password", "wrong"))
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
