package org.gym.workload.service;

import org.gym.workload.dto.ActionType;
import org.gym.workload.dto.MonthSummary;
import org.gym.workload.dto.TrainerSummaryResponse;
import org.gym.workload.dto.TrainerWorkloadRequest;
import org.gym.workload.dto.YearSummary;
import org.gym.workload.model.Month;
import org.gym.workload.model.TrainerWorkload;
import org.gym.workload.model.Year;
import org.gym.workload.repository.TrainerWorkloadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class WorkloadService {

    private static final Logger log = LoggerFactory.getLogger(WorkloadService.class);

    private final TrainerWorkloadRepository repository;

    public WorkloadService(TrainerWorkloadRepository repository) {
        this.repository = repository;
    }

    public void updateWorkload(TrainerWorkloadRequest request) {
        int year = request.trainingDate().getYear();
        int month = request.trainingDate().getMonthValue();

        TrainerWorkload workload = repository.findByUsername(request.username())
                .orElseGet(() -> {
                    log.info("No workload document found for trainer {}, creating a new one", request.username());
                    return new TrainerWorkload(request.username(), request.firstName(),
                            request.lastName(), request.active());
                });

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

        repository.save(workload);
        log.info("Persisted workload document for trainer {}", request.username());
    }

    public TrainerSummaryResponse getSummary(String username) {
        TrainerWorkload workload = repository.findByUsername(username).orElse(null);
        if (workload == null) {
            log.info("No workload document found for trainer {}", username);
            return new TrainerSummaryResponse(username, null, null, false, List.of());
        }

        List<YearSummary> years = workload.getYears().stream()
                .sorted(Comparator.comparingInt(Year::getYear))
                .map(this::toYearSummary)
                .toList();

        log.info("Returning workload summary for trainer {}", username);
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
