package org.gym.service;

import org.core.entities.Users;
import org.gym.exception.AuthenticationException;
import org.gym.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UsersRepository usersRepository;
    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private LoginService loginService;

    @Test
    void changeLoginUpdatesPasswordWhenOldMatches() {
        Users user = new Users();
        user.setUserName("john.doe1");
        user.setPassword("oldPass1234");
        when(usersRepository.findByUsername("john.doe1")).thenReturn(Optional.of(user));

        loginService.changeLogin("john.doe1", "oldPass1234", "newPass5678");

        assertEquals("newPass5678", user.getPassword());
        verify(usersRepository).save(user);
    }

    @Test
    void changeLoginRejectsWrongOldPassword() {
        Users user = new Users();
        user.setUserName("john.doe1");
        user.setPassword("oldPass1234");
        when(usersRepository.findByUsername("john.doe1")).thenReturn(Optional.of(user));

        assertThrows(AuthenticationException.class,
                () -> loginService.changeLogin("john.doe1", "wrong", "newPass5678"));
    }
}
