package org.gym.workload.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record TrainerWorkloadRequest(
        @NotBlank(message = "Trainer username is required") String username,
        @NotBlank(message = "Trainer first name is required") String firstName,
        @NotBlank(message = "Trainer last name is required") String lastName,
        @NotNull(message = "Active flag is required") Boolean active,
        @NotNull(message = "Training date is required") LocalDate trainingDate,
        @NotNull(message = "Training duration is required") @Positive(message = "Training duration must be positive") Double trainingDuration,
        @NotNull(message = "Action type is required") ActionType actionType) {
}
