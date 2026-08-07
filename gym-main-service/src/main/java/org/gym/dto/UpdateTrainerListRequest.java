package org.gym.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record UpdateTrainerListRequest(
        @NotEmpty(message = "At least one trainer username is required") List<String> trainerUsernames) {
}
