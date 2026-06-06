package org.core.repositories.TrainingTypeRepository;

import org.core.entities.TrainingType;
import org.core.repositories.TrainingRepository.TrainingRepositoryAbstraction;

import java.util.Optional;

public class TrainingTypeRepo implements TrainingTypeRepoAbstraction<TrainingType> {
    @Override
    public Optional<TrainingType> selectById(Integer id) {
        return Optional.empty();
    }
}
