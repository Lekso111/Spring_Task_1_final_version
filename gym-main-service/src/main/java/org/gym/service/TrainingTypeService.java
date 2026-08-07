package org.gym.service;

import org.gym.dto.TrainingTypeResponse;
import org.gym.mapper.GymMapper;
import org.gym.repository.TrainingTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;
    private final GymMapper mapper;

    public TrainingTypeService(TrainingTypeRepository trainingTypeRepository, GymMapper mapper) {
        this.trainingTypeRepository = trainingTypeRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<TrainingTypeResponse> getAll() {
        return trainingTypeRepository.findAll().stream()
                .map(mapper::toTrainingTypeResponse)
                .toList();
    }
}
