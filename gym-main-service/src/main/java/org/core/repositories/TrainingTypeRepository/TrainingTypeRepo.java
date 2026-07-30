package org.core.repositories.TrainingTypeRepository;

import org.core.entities.TrainingType;

import java.util.Optional;

public class TrainingTypeRepo implements TrainingTypeRepoInterface<TrainingType> {
    @Override
    public Optional<TrainingType> selectById(Integer id) {
        return Optional.empty();
    }
}
