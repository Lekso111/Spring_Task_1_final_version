package org.gym.workload.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.gym.workload.dto.TrainerSummaryResponse;
import org.gym.workload.service.WorkloadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trainers/workload")
@Tag(name = "Workload", description = "Trainer workload management")
public class WorkloadController {

    private final WorkloadService workloadService;

    public WorkloadController(WorkloadService workloadService) {
        this.workloadService = workloadService;
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get a trainer monthly workload summary")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Summary returned")
    })
    public ResponseEntity<TrainerSummaryResponse> getSummary(@PathVariable String username) {
        return ResponseEntity.ok(workloadService.getSummary(username));
    }
}
