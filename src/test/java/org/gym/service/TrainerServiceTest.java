package org.gym.service;

import org.core.entities.Trainer;
import org.core.entities.TrainingType;
import org.gym.dto.RegistrationResponse;
import org.gym.dto.TrainerRegistrationRequest;
import org.gym.exception.ResourceNotFoundException;
import org.gym.mapper.GymMapper;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingRepository;
import org.gym.repository.TrainingTypeRepository;
import org.gym.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private TrainingTypeRepository trainingTypeRepository;
    @Mock
    private CredentialGenerator credentialGenerator;
    @Mock
    private GymMapper mapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    void registerCreatesTrainerWithSpecialization() {
        when(trainingTypeRepository.findById(1L)).thenReturn(Optional.of(new TrainingType()));
        when(credentialGenerator.generateUsername("amy", "lee")).thenReturn("amy.lee1");
        when(credentialGenerator.generatePassword()).thenReturn("pass123456");
        when(passwordEncoder.encode("pass123456")).thenReturn("hashed-pass");
        when(jwtService.generateToken("amy.lee1")).thenReturn("a.jwt.token");

        RegistrationResponse response = trainerService.register(new TrainerRegistrationRequest("amy", "lee", 1L));

        assertEquals("amy.lee1", response.username());
        assertEquals("pass123456", response.password());
        assertEquals("a.jwt.token", response.token());
        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    void registerThrowsWhenSpecializationMissing() {
        when(trainingTypeRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> trainerService.register(new TrainerRegistrationRequest("amy", "lee", 99L)));
    }
}
