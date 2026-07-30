package org.gym.service;

import org.core.entities.User;
import org.gym.dto.LoginResponse;
import org.gym.exception.AuthenticationException;
import org.gym.repository.UsersRepository;
import org.gym.security.JwtService;
import org.gym.security.LoginAttemptService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UsersRepository usersRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private LoginAttemptService loginAttemptService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginService loginService;

    @Test
    void loginReturnsTokenForValidCredentials() {
        when(loginAttemptService.isBlocked("john.doe1")).thenReturn(false);
        when(jwtService.generateToken("john.doe1")).thenReturn("a.jwt.token");

        LoginResponse response = loginService.login("john.doe1", "secret12345");

        assertEquals("a.jwt.token", response.token());
        verify(loginAttemptService).loginSucceeded("john.doe1");
    }

    @Test
    void loginRecordsFailureForBadCredentials() {
        when(loginAttemptService.isBlocked("john.doe1")).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("bad"));

        assertThrows(AuthenticationException.class, () -> loginService.login("john.doe1", "wrong"));
        verify(loginAttemptService).loginFailed("john.doe1");
    }

    @Test
    void loginRejectsBlockedUser() {
        when(loginAttemptService.isBlocked("john.doe1")).thenReturn(true);

        assertThrows(AuthenticationException.class, () -> loginService.login("john.doe1", "secret12345"));
        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    void changeLoginUpdatesPasswordWhenOldMatches() {
        User user = new User();
        user.setUserName("john.doe1");
        user.setPassword("hashed-old");
        when(usersRepository.findByUsername("john.doe1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPass1234", "hashed-old")).thenReturn(true);
        when(passwordEncoder.encode("newPass5678")).thenReturn("hashed-new");

        loginService.changeLogin("john.doe1", "oldPass1234", "newPass5678");

        assertEquals("hashed-new", user.getPassword());
        verify(usersRepository).save(user);
    }

    @Test
    void changeLoginRejectsWrongOldPassword() {
        User user = new User();
        user.setUserName("john.doe1");
        user.setPassword("hashed-old");
        when(usersRepository.findByUsername("john.doe1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashed-old")).thenReturn(false);

        assertThrows(AuthenticationException.class,
                () -> loginService.changeLogin("john.doe1", "wrong", "newPass5678"));
    }
}
