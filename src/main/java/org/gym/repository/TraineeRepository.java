package org.gym.repository;

import org.core.entities.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Integer> {

    @Query("SELECT t FROM Trainee t WHERE t.username = :username")
    Optional<Trainee> findByUsername(@Param("username") String username);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Trainee t WHERE t.username = :username")
    boolean existsByUsername(@Param("username") String username);
}
