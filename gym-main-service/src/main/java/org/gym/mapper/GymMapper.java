package org.gym.mapper;

import org.core.entities.Trainee;
import org.core.entities.Trainer;
import org.core.entities.Training;
import org.core.entities.TrainingType;
import org.gym.dto.TraineeProfileResponse;
import org.gym.dto.TraineeSummaryResponse;
import org.gym.dto.TrainerProfileResponse;
import org.gym.dto.TrainerSummaryResponse;
import org.gym.dto.TrainingResponse;
import org.gym.dto.TrainingTypeResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GymMapper {

    public TrainingTypeResponse toTrainingTypeResponse(TrainingType trainingType) {
        if (trainingType == null) {
            return null;
        }
        return new TrainingTypeResponse(trainingType.getId(), trainingType.getName());
    }

    public TrainerSummaryResponse toTrainerSummary(Trainer trainer) {
        return new TrainerSummaryResponse(
                trainer.getUser().getUserName(),
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                toTrainingTypeResponse(trainer.getTrainingType()));
    }

    public TraineeSummaryResponse toTraineeSummary(Trainee trainee) {
        return new TraineeSummaryResponse(
                trainee.getUser().getUserName(),
                trainee.getUser().getFirstName(),
                trainee.getUser().getLastName());
    }

    public TraineeProfileResponse toTraineeProfile(Trainee trainee) {
        List<TrainerSummaryResponse> trainers = trainee.getTrainers().stream()
                .map(this::toTrainerSummary)
                .toList();
        return new TraineeProfileResponse(
                trainee.getUser().getUserName(),
                trainee.getUser().getFirstName(),
                trainee.getUser().getLastName(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainee.getUser().isActive(),
                trainers);
    }

    public TrainerProfileResponse toTrainerProfile(Trainer trainer) {
        List<TraineeSummaryResponse> trainees = trainer.getTrainees().stream()
                .map(this::toTraineeSummary)
                .toList();
        return new TrainerProfileResponse(
                trainer.getUser().getUserName(),
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                toTrainingTypeResponse(trainer.getTrainingType()),
                trainer.getUser().isActive(),
                trainees);
    }

    public TrainingResponse toTraineeTrainingResponse(Training training) {
        String partner = training.getTrainer() == null
                ? null
                : fullName(training.getTrainer().getUser().getFirstName(), training.getTrainer().getUser().getLastName());
        return new TrainingResponse(
                training.getName(),
                training.getDate(),
                typeName(training.getTrainingType()),
                training.getDuration(),
                partner);
    }

    public TrainingResponse toTrainerTrainingResponse(Training training) {
        String partner = training.getTrainee() == null
                ? null
                : fullName(training.getTrainee().getUser().getFirstName(), training.getTrainee().getUser().getLastName());
        return new TrainingResponse(
                training.getName(),
                training.getDate(),
                typeName(training.getTrainingType()),
                training.getDuration(),
                partner);
    }

    private String typeName(TrainingType trainingType) {
        return trainingType == null ? null : trainingType.getName();
    }

    private String fullName(String firstName, String lastName) {
        return firstName + " " + lastName;
    }
}
