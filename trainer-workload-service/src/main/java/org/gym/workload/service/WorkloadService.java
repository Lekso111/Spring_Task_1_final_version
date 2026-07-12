package org.gym.workload.service;

import org.gym.workload.dto.ActionType;
import org.gym.workload.dto.MonthSummary;
import org.gym.workload.dto.TrainerSummaryResponse;
import org.gym.workload.dto.TrainerWorkloadRequest;
import org.gym.workload.dto.YearSummary;
import org.gym.workload.model.Month;
import org.gym.workload.model.TrainerWorkload;
import org.gym.workload.model.Year;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WorkloadService {

    private static final Logger log = LoggerFactory.getLogger(WorkloadService.class);

    private final Map<String, TrainerWorkload> storage = new ConcurrentHashMap<>();

    public synchronized void updateWorkload(TrainerWorkloadRequest request) {
        int year = request.trainingDate().getYear();
        int month = request.trainingDate().getMonthValue();

        TrainerWorkload workload = storage.computeIfAbsent(request.username(),
                key -> new TrainerWorkload(request.username(), request.firstName(), request.lastName(), request.active()));
        workload.setFirstName(request.firstName());
        workload.setLastName(request.lastName());
        workload.setActive(request.active());

        if (request.actionType() == ActionType.ADD) {
            workload.addDuration(year, month, request.trainingDuration());
            log.info("Added {} hours for trainer {} in {}-{}",
                    request.trainingDuration(), request.username(), year, month);
        } else {
            workload.subtractDuration(year, month, request.trainingDuration());
            log.info("Removed {} hours for trainer {} in {}-{}",
                    request.trainingDuration(), request.username(), year, month);
        }
    }

    public synchronized TrainerSummaryResponse getSummary(String username) {
        TrainerWorkload workload = storage.get(username);
        if (workload == null) {
            return new TrainerSummaryResponse(username, null, null, false, List.of());
        }

        List<YearSummary> years = workload.getYears().stream()
                .sorted(Comparator.comparingInt(Year::getYear))
                .map(this::toYearSummary)
                .toList();

        return new TrainerSummaryResponse(workload.getUsername(), workload.getFirstName(),
                workload.getLastName(), workload.isActive(), years);
    }

    private YearSummary toYearSummary(Year year) {
        List<MonthSummary> months = year.getMonths().stream()
                .sorted(Comparator.comparingInt(Month::getMonth))
                .map(month -> new MonthSummary(month.getMonth(), month.getSummaryDuration()))
                .toList();
        return new YearSummary(year.getYear(), months);
    }
}
