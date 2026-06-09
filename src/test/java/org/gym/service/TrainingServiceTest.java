package org.gym.service;

import org.core.entities.Trainee;
import org.core.entities.Trainer;
import org.core.entities.Training;
import org.core.entities.TrainingType;
import org.gym.dto.AddTrainingRequest;
import org.gym.exception.ResourceNotFoundException;
import org.gym.repository.TraineeRepository;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainingService trainingService;

    @Test
    void addTrainingInheritsTrainerSpecialization() {
        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        TrainingType type = new TrainingType();
        trainer.setTrainingType(type);

        when(traineeRepository.findByUsername("john.doe1")).thenReturn(Optional.of(trainee));
        when(trainerRepository.findByUsername("amy.lee1")).thenReturn(Optional.of(trainer));

        AddTrainingRequest request = new AddTrainingRequest(
                "john.doe1", "amy.lee1", "Morning Cardio", LocalDate.parse("2026-01-10"), 60.0);
        trainingService.addTraining(request);

        ArgumentCaptor<Training> captor = ArgumentCaptor.forClass(Training.class);
        verify(trainingRepository).save(captor.capture());
        Training saved = captor.getValue();
        assertEquals("Morning Cardio", saved.getName());
        assertEquals(60.0, saved.getDuration());
        assertSame(type, saved.getTrainingType());
    }

    @Test
    void addTrainingThrowsWhenTraineeMissing() {
        when(traineeRepository.findByUsername("missing")).thenReturn(Optional.empty());
        AddTrainingRequest request = new AddTrainingRequest(
                "missing", "amy.lee1", "Morning Cardio", LocalDate.parse("2026-01-10"), 60.0);
        assertThrows(ResourceNotFoundException.class, () -> trainingService.addTraining(request));
    }
}
