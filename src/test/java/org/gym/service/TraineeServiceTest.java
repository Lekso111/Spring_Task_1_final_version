package org.gym.service;

import org.core.entities.Trainee;
import org.gym.dto.RegistrationResponse;
import org.gym.dto.TraineeProfileResponse;
import org.gym.dto.TraineeRegistrationRequest;
import org.gym.exception.BusinessValidationException;
import org.gym.exception.ResourceNotFoundException;
import org.gym.mapper.GymMapper;
import org.gym.repository.TraineeRepository;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private CredentialGenerator credentialGenerator;
    @Mock
    private GymMapper mapper;

    @InjectMocks
    private TraineeService traineeService;

    @Test
    void registerGeneratesCredentialsAndPersists() {
        when(credentialGenerator.generateUsername("john", "doe")).thenReturn("john.doe1");
        when(credentialGenerator.generatePassword()).thenReturn("secret12345");

        TraineeRegistrationRequest request = new TraineeRegistrationRequest("john", "doe", null, "street");
        RegistrationResponse response = traineeService.register(request);

        assertEquals("john.doe1", response.username());
        assertEquals("secret12345", response.password());
        verify(traineeRepository).save(any(Trainee.class));
    }

    @Test
    void getProfileReturnsMappedResponse() {
        Trainee trainee = new Trainee();
        TraineeProfileResponse expected = new TraineeProfileResponse("john.doe1", "john", "doe", null, "street", true, List.of());
        when(traineeRepository.findByUsername("john.doe1")).thenReturn(Optional.of(trainee));
        when(mapper.toTraineeProfile(trainee)).thenReturn(expected);

        assertEquals(expected, traineeService.getProfile("john.doe1"));
    }

    @Test
    void getProfileThrowsWhenMissing() {
        when(traineeRepository.findByUsername("missing")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> traineeService.getProfile("missing"));
    }

    @Test
    void setActiveStatusIsNotIdempotent() {
        Trainee trainee = new Trainee();
        trainee.setActive(true);
        when(traineeRepository.findByUsername(anyString())).thenReturn(Optional.of(trainee));

        assertThrows(BusinessValidationException.class, () -> traineeService.setActiveStatus("john.doe1", true));
    }

    @Test
    void setActiveStatusUpdatesWhenStateChanges() {
        Trainee trainee = new Trainee();
        trainee.setActive(true);
        when(traineeRepository.findByUsername(anyString())).thenReturn(Optional.of(trainee));

        traineeService.setActiveStatus("john.doe1", false);

        verify(traineeRepository).save(trainee);
        assertEquals(false, trainee.getUser().isActive());
    }
}
