package org.gym.repository;

import org.core.entities.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Integer> {

    @Query("SELECT t FROM Training t WHERE t.trainee.username = :username "
            + "AND (:fromDate IS NULL OR t.date >= :fromDate) "
            + "AND (:toDate IS NULL OR t.date <= :toDate) "
            + "AND (:trainerName IS NULL OR t.trainer.firstName = :trainerName) "
            + "AND (:trainingTypeId IS NULL OR t.trainingType.id = :trainingTypeId)")
    List<Training> findTraineeTrainings(@Param("username") String username,
                                        @Param("fromDate") LocalDate fromDate,
                                        @Param("toDate") LocalDate toDate,
                                        @Param("trainerName") String trainerName,
                                        @Param("trainingTypeId") Long trainingTypeId);

    @Query("SELECT t FROM Training t WHERE t.trainer.username = :username "
            + "AND (:fromDate IS NULL OR t.date >= :fromDate) "
            + "AND (:toDate IS NULL OR t.date <= :toDate) "
            + "AND (:traineeName IS NULL OR t.trainee.firstName = :traineeName)")
    List<Training> findTrainerTrainings(@Param("username") String username,
                                        @Param("fromDate") LocalDate fromDate,
                                        @Param("toDate") LocalDate toDate,
                                        @Param("traineeName") String traineeName);

    @Modifying
    @Query("DELETE FROM Training t WHERE t.trainee.id = :traineeId")
    void deleteByTraineeId(@Param("traineeId") Integer traineeId);
}
