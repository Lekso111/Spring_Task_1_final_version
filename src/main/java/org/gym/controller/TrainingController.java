package org.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.gym.dto.AddTrainingRequest;
import org.gym.service.TrainingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trainings")
@Tag(name = "Training", description = "Training management")
public class TrainingController {

    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping
    @Operation(summary = "Add a training")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Training added"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Trainee or trainer not found")
    })
    public ResponseEntity<Void> addTraining(@Valid @RequestBody AddTrainingRequest request) {
        trainingService.addTraining(request);
        return ResponseEntity.ok().build();
    }
}
