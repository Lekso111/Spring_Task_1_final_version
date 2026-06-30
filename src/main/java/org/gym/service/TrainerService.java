package org.gym.service;

import org.core.entities.Trainer;
import org.core.entities.TrainingType;
import org.core.entities.Users;
import org.gym.dto.RegistrationResponse;
import org.gym.dto.TrainerProfileResponse;
import org.gym.dto.TrainerRegistrationRequest;
import org.gym.dto.TrainingResponse;
import org.gym.dto.UpdateTrainerProfileRequest;
import org.gym.exception.BusinessValidationException;
import org.gym.exception.ResourceNotFoundException;
import org.gym.mapper.GymMapper;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingRepository;
import org.gym.repository.TrainingTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TrainerService {

    private static final Logger log = LoggerFactory.getLogger(TrainerService.class);

    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final CredentialGenerator credentialGenerator;
    private final GymMapper mapper;

    public TrainerService(TrainerRepository trainerRepository,
                          TrainingRepository trainingRepository,
                          TrainingTypeRepository trainingTypeRepository,
                          CredentialGenerator credentialGenerator,
                          GymMapper mapper) {
        this.trainerRepository = trainerRepository;
        this.trainingRepository = trainingRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.credentialGenerator = credentialGenerator;
        this.mapper = mapper;
    }

    @Transactional
    public RegistrationResponse register(TrainerRegistrationRequest request) {
        TrainingType specialization = trainingTypeRepository.findById(request.specializationId())
                .orElseThrow(() -> new ResourceNotFoundException("Training type not found: " + request.specializationId()));

        String username = credentialGenerator.generateUsername(request.firstName(), request.lastName());
        String password = credentialGenerator.generatePassword();

        Trainer trainer = new Trainer();
        trainer.setFirstName(request.firstName());
        trainer.setLastName(request.lastName());
        trainer.getUser().setUserName(username);
        trainer.getUser().setPassword(password);
        trainer.setActiveStatus(true);
        trainer.setTrainingType(specialization);

        trainerRepository.save(trainer);
        log.info("Registered trainer with username {}", username);
        return new RegistrationResponse(username, password);
    }

    @Transactional(readOnly = true)
    public TrainerProfileResponse getProfile(String username) {
        Trainer trainer = require(username);
        return mapper.toTrainerProfile(trainer);
    }

    @Transactional
    public TrainerProfileResponse updateProfile(UpdateTrainerProfileRequest request) {
        Trainer trainer = require(request.username());
        trainer.setFirstName(request.firstName());
        trainer.setLastName(request.lastName());
        trainer.setActiveStatus(request.active());
        trainerRepository.save(trainer);
        return mapper.toTrainerProfile(trainer);
    }

    @Transactional
    public void setActiveStatus(String username, boolean active) {
        Trainer trainer = require(username);
        Users user = trainer.getUser();
        if (user.isActive() == active) {
            throw new BusinessValidationException("Trainer is already " + (active ? "active" : "inactive"));
        }
        trainer.setActiveStatus(active);
        trainerRepository.save(trainer);
    }

    @Transactional(readOnly = true)
    public List<TrainingResponse> getTrainings(String username, LocalDate fromDate, LocalDate toDate,
                                               String traineeName) {
        require(username);
        return trainingRepository.findTrainerTrainings(username, fromDate, toDate, traineeName).stream()
                .map(mapper::toTrainerTrainingResponse)
                .toList();
    }

    private Trainer require(String username) {
        return trainerRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found: " + username));
    }
}
