package org.gym.service;

import org.core.entities.Trainee;
import org.core.entities.Trainer;
import org.core.entities.Users;
import org.gym.dto.RegistrationResponse;
import org.gym.dto.TraineeProfileResponse;
import org.gym.dto.TraineeRegistrationRequest;
import org.gym.dto.TrainerSummaryResponse;
import org.gym.dto.TrainingResponse;
import org.gym.dto.UpdateTraineeProfileRequest;
import org.gym.exception.BusinessValidationException;
import org.gym.exception.ResourceNotFoundException;
import org.gym.mapper.GymMapper;
import org.gym.repository.TraineeRepository;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TraineeService {

    private static final Logger log = LoggerFactory.getLogger(TraineeService.class);

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final CredentialGenerator credentialGenerator;
    private final GymMapper mapper;

    public TraineeService(TraineeRepository traineeRepository,
                          TrainerRepository trainerRepository,
                          TrainingRepository trainingRepository,
                          CredentialGenerator credentialGenerator,
                          GymMapper mapper) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingRepository = trainingRepository;
        this.credentialGenerator = credentialGenerator;
        this.mapper = mapper;
    }

    @Transactional
    public RegistrationResponse register(TraineeRegistrationRequest request) {
        String username = credentialGenerator.generateUsername(request.firstName(), request.lastName());
        String password = credentialGenerator.generatePassword();

        Trainee trainee = new Trainee();
        trainee.setFirstName(request.firstName());
        trainee.setLastName(request.lastName());
        if (request.dateOfBirth() != null) {
            trainee.setDateOfBirth(request.dateOfBirth().toString());
        }
        trainee.setAddress(request.address());
        trainee.getUser().setUserName(username);
        trainee.getUser().setPassword(password);
        trainee.setActive(true);

        traineeRepository.save(trainee);
        log.info("Registered trainee with username {}", username);
        return new RegistrationResponse(username, password);
    }

    @Transactional(readOnly = true)
    public TraineeProfileResponse getProfile(String username) {
        Trainee trainee = require(username);
        return mapper.toTraineeProfile(trainee);
    }

    @Transactional
    public TraineeProfileResponse updateProfile(UpdateTraineeProfileRequest request) {
        Trainee trainee = require(request.username());
        trainee.setFirstName(request.firstName());
        trainee.setLastName(request.lastName());
        if (request.dateOfBirth() != null) {
            trainee.setDateOfBirth(request.dateOfBirth().toString());
        }
        trainee.setAddress(request.address());
        trainee.setActive(request.active());
        traineeRepository.save(trainee);
        return mapper.toTraineeProfile(trainee);
    }

    @Transactional
    public void delete(String username) {
        Trainee trainee = require(username);
        trainingRepository.deleteByTraineeId(trainee.getId());
        for (Trainer trainer : trainee.getTrainers()) {
            trainer.getTrainees().remove(trainee);
        }
        trainee.getTrainers().clear();
        traineeRepository.delete(trainee);
        log.info("Deleted trainee with username {}", username);
    }

    @Transactional(readOnly = true)
    public List<TrainerSummaryResponse> getUnassignedActiveTrainers(String username) {
        require(username);
        return trainerRepository.findUnassignedActiveTrainers(username).stream()
                .map(mapper::toTrainerSummary)
                .toList();
    }

    @Transactional
    public List<TrainerSummaryResponse> updateTrainerList(String username, List<String> trainerUsernames) {
        Trainee trainee = require(username);
        List<Trainer> trainers = trainerRepository.findByUsernames(trainerUsernames);
        if (trainers.size() != trainerUsernames.stream().distinct().count()) {
            throw new ResourceNotFoundException("One or more trainers were not found");
        }
        for (Trainer trainer : trainee.getTrainers()) {
            trainer.getTrainees().remove(trainee);
        }
        trainee.getTrainers().clear();
        Set<Trainer> updated = new HashSet<>(trainers);
        for (Trainer trainer : updated) {
            trainee.getTrainers().add(trainer);
            trainer.getTrainees().add(trainee);
        }
        traineeRepository.save(trainee);
        return trainee.getTrainers().stream()
                .map(mapper::toTrainerSummary)
                .toList();
    }

    @Transactional
    public void setActiveStatus(String username, boolean active) {
        Trainee trainee = require(username);
        Users user = trainee.getUser();
        if (user.isActive() == active) {
            throw new BusinessValidationException("Trainee is already " + (active ? "active" : "inactive"));
        }
        trainee.setActive(active);
        traineeRepository.save(trainee);
    }

    @Transactional(readOnly = true)
    public List<TrainingResponse> getTrainings(String username, LocalDate fromDate, LocalDate toDate,
                                               String trainerName, Long trainingTypeId) {
        require(username);
        return trainingRepository.findTraineeTrainings(username, fromDate, toDate, trainerName, trainingTypeId).stream()
                .map(mapper::toTraineeTrainingResponse)
                .toList();
    }

    private Trainee require(String username) {
        return traineeRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found: " + username));
    }
}
