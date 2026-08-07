package org.core.repositories.TrainingRepository;

import java.util.Optional;

public interface TrainingRepositoryInterface<T> {

    public void add(T t);
    public Optional<T> selectById(Integer id);

}
