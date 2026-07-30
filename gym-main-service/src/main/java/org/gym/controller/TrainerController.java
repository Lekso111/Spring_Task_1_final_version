package org.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.gym.dto.ActiveStatusRequest;
import org.gym.dto.RegistrationResponse;
import org.gym.dto.TrainerProfileResponse;
import org.gym.dto.TrainerRegistrationRequest;
import org.gym.dto.TrainingResponse;
import org.gym.dto.UpdateTrainerProfileRequest;
import org.gym.service.TrainerService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/trainers")
@Tag(name = "Trainer", description = "Trainer registration and profile management")
public class TrainerController {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new trainer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trainer registered"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Training type not found")
    })
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody TrainerRegistrationRequest request) {
        return ResponseEntity.ok(trainerService.register(request));
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get trainer profile by username")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trainer profile returned"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    public ResponseEntity<TrainerProfileResponse> getProfile(@PathVariable String username) {
        return ResponseEntity.ok(trainerService.getProfile(username));
    }

    @PutMapping
    @Operation(summary = "Update trainer profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trainer profile updated"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    public ResponseEntity<TrainerProfileResponse> updateProfile(@Valid @RequestBody UpdateTrainerProfileRequest request) {
        return ResponseEntity.ok(trainerService.updateProfile(request));
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "Get trainer trainings list with optional filters")
    public ResponseEntity<List<TrainingResponse>> getTrainings(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodTo,
            @RequestParam(required = false) String traineeName) {
        return ResponseEntity.ok(trainerService.getTrainings(username, periodFrom, periodTo, traineeName));
    }

    @PatchMapping("/{username}/status")
    @Operation(summary = "Activate or de-activate a trainer")
    public ResponseEntity<Void> setStatus(@PathVariable String username,
                                          @Valid @RequestBody ActiveStatusRequest request) {
        trainerService.setActiveStatus(username, request.active());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
