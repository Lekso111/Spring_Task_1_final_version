package org.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.gym.dto.RegistrationResponse;
import org.gym.dto.TraineeProfileResponse;
import org.gym.dto.TraineeRegistrationRequest;
import org.gym.dto.TrainerSummaryResponse;
import org.gym.dto.TrainingResponse;
import org.gym.dto.UpdateTraineeProfileRequest;
import org.gym.dto.UpdateTrainerListRequest;
import org.gym.dto.ActiveStatusRequest;
import org.gym.service.TraineeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trainees")
@Tag(name = "Trainee", description = "Trainee registration and profile management")
public class TraineeController {

    private final TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new trainee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trainee registered"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody TraineeRegistrationRequest request) {
        return ResponseEntity.ok(traineeService.register(request));
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get trainee profile by username")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trainee profile returned"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    public ResponseEntity<TraineeProfileResponse> getProfile(@PathVariable String username) {
        return ResponseEntity.ok(traineeService.getProfile(username));
    }

    @PutMapping
    @Operation(summary = "Update trainee profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trainee profile updated"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    public ResponseEntity<TraineeProfileResponse> updateProfile(@Valid @RequestBody UpdateTraineeProfileRequest request) {
        return ResponseEntity.ok(traineeService.updateProfile(request));
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete trainee profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trainee deleted"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    public ResponseEntity<Void> delete(@PathVariable String username) {
        traineeService.delete(username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/unassigned-trainers")
    @Operation(summary = "Get active trainers not assigned to the trainee")
    public ResponseEntity<List<TrainerSummaryResponse>> getUnassignedTrainers(@PathVariable String username) {
        return ResponseEntity.ok(traineeService.getUnassignedActiveTrainers(username));
    }

    @PutMapping("/{username}/trainers")
    @Operation(summary = "Update the trainee's trainer list")
    public ResponseEntity<List<TrainerSummaryResponse>> updateTrainers(@PathVariable String username,
                                                                       @Valid @RequestBody UpdateTrainerListRequest request) {
        return ResponseEntity.ok(traineeService.updateTrainerList(username, request.trainerUsernames()));
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "Get trainee trainings list with optional filters")
    public ResponseEntity<List<TrainingResponse>> getTrainings(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodTo,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) Long trainingTypeId) {
        return ResponseEntity.ok(traineeService.getTrainings(username, periodFrom, periodTo, trainerName, trainingTypeId));
    }

    @PatchMapping("/{username}/status")
    @Operation(summary = "Activate or de-activate a trainee")
    public ResponseEntity<Void> setStatus(@PathVariable String username,
                                          @Valid @RequestBody ActiveStatusRequest request) {
        traineeService.setActiveStatus(username, request.active());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
