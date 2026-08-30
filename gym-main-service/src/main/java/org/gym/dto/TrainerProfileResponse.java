package org.gym.dto;

import java.util.List;

public record TrainerProfileResponse(
        String username,
        String firstName,
        String lastName,
        TrainingTypeResponse specialization,
        boolean active,
        List<TraineeSummaryResponse> trainees) {
}
