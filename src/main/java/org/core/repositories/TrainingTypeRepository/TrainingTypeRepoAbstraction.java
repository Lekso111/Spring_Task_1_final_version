package org.core.repositories.TrainingTypeRepository;

import java.util.Optional;

public interface TrainingTypeRepoAbstraction<T> {
    public Optional<T> selectById(Integer id);
}
