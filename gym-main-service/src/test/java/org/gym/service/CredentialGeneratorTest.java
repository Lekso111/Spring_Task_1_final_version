package org.gym.service;

import org.gym.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CredentialGeneratorTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private CredentialGenerator credentialGenerator;

    @Test
    void generatesFirstAvailableSerial() {
        when(usersRepository.existsByUsername("john.doe1")).thenReturn(true);
        when(usersRepository.existsByUsername("john.doe2")).thenReturn(false);

        assertEquals("john.doe2", credentialGenerator.generateUsername("john", "doe"));
    }

    @Test
    void generatesPasswordOfExpectedLength() {
        assertEquals(11, credentialGenerator.generatePassword().length());
    }
}
