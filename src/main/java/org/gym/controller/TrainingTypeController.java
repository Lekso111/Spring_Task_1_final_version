package org.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.gym.dto.TrainingTypeResponse;
import org.gym.service.TrainingTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/training-types")
@Tag(name = "Training Types", description = "Reference list of training types")
public class TrainingTypeController {

    private final TrainingTypeService trainingTypeService;

    public TrainingTypeController(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    @GetMapping
    @Operation(summary = "Get all training types")
    public ResponseEntity<List<TrainingTypeResponse>> getTrainingTypes() {
        return ResponseEntity.ok(trainingTypeService.getAll());
    }
}
