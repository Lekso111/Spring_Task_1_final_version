package org.core.repositories.TrainingTypeRepository;

import java.util.Optional;

public interface TrainingTypeRepoInterface<T> {
    public Optional<T> selectById(Integer id);
}
