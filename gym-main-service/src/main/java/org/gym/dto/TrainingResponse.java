package org.gym.dto;

import java.time.LocalDate;

public record TrainingResponse(
        String name,
        LocalDate date,
        String trainingType,
        double duration,
        String partnerName) {
}
