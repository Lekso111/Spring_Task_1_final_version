package org.gym.service;

import org.core.entities.Trainee;
import org.core.entities.Trainer;
import org.core.entities.Training;
import org.gym.dto.AddTrainingRequest;
import org.gym.exception.ResourceNotFoundException;
import org.gym.repository.TraineeRepository;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingRepository;
import org.gym.workload.ActionType;
import org.gym.workload.WorkloadNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrainingService {

    private static final Logger log = LoggerFactory.getLogger(TrainingService.class);

    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final WorkloadNotificationService workloadNotificationService;

    public TrainingService(TrainingRepository trainingRepository,
                           TraineeRepository traineeRepository,
                           TrainerRepository trainerRepository,
                           WorkloadNotificationService workloadNotificationService) {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.workloadNotificationService = workloadNotificationService;
    }

    @Transactional
    public void addTraining(AddTrainingRequest request) {
        Trainee trainee = traineeRepository.findByUsername(request.traineeUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found: " + request.traineeUsername()));
        Trainer trainer = trainerRepository.findByUsername(request.trainerUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found: " + request.trainerUsername()));

        Training training = new Training();
        training.setName(request.trainingName());
        training.setDate(request.trainingDate().toString());
        training.setDuration(request.trainingDuration());
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainer.getTrainingType());

        trainee.getTrainers().add(trainer);
        trainer.getTrainees().add(trainee);

        trainingRepository.save(training);
        log.info("Added training {} for trainee {} with trainer {}",
                request.trainingName(), request.traineeUsername(), request.trainerUsername());

        workloadNotificationService.notify(training, ActionType.ADD);
    }

    @Transactional
    public void deleteTraining(Integer id) {
        Training training = trainingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training not found: " + id));

        Trainee trainee = training.getTrainee();
        Trainer trainer = training.getTrainer();
        if (trainee != null) {
            trainee.getTrainers().remove(trainer);
        }

        trainingRepository.delete(training);
        log.info("Deleted training {}", id);

        workloadNotificationService.notify(training, ActionType.DELETE);
    }
}
