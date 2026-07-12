package org.gym.workload.dto;

import java.util.List;

public record YearSummary(int year, List<MonthSummary> months) {
}
