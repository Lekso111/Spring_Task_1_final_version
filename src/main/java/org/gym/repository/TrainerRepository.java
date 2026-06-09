package org.gym.repository;

import org.core.entities.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Integer> {

    @Query("SELECT t FROM Trainer t WHERE t.userId.username = :username")
    Optional<Trainer> findByUsername(@Param("username") String username);

    @Query("SELECT t FROM Trainer t WHERE t.userId.username IN :usernames")
    List<Trainer> findByUsernames(@Param("usernames") List<String> usernames);

    @Query("SELECT tr FROM Trainer tr WHERE tr.userId.isActive = true AND tr NOT IN "
            + "(SELECT t FROM Trainee te JOIN te.trainers t WHERE te.userId.username = :username)")
    List<Trainer> findUnassignedActiveTrainers(@Param("username") String username);
}
