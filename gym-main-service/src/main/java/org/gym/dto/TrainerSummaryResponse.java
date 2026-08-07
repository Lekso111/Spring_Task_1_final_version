package org.gym.dto;

public record TrainerSummaryResponse(
        String username,
        String firstName,
        String lastName,
        TrainingTypeResponse specialization) {
}
