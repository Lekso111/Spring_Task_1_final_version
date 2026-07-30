package org.gym.workload.dto;

import java.util.List;

public record TrainerSummaryResponse(
        String username,
        String firstName,
        String lastName,
        boolean status,
        List<YearSummary> years) {
}
