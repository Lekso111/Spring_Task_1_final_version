package org.gym.workload;

import java.time.LocalDate;

public record TrainerWorkloadRequest(
        String username,
        String firstName,
        String lastName,
        boolean active,
        LocalDate trainingDate,
        double trainingDuration,
        ActionType actionType) {
}
