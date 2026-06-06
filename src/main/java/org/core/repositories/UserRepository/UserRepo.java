package org.core.repositories.UserRepository;

import org.core.entities.Training;
import org.core.entities.TrainingType;
import org.core.entities.Users;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepo<T> {


    public void add(T user);
    public void update(T user,String username,String password,boolean activeStatus) throws Exception;
    public Optional<T> select(String username,String password) throws Exception;
    public  boolean verify(T t, String username, String password) throws Exception;
    public Users selectUser(String username,String password) throws Exception;
    public List<Training> getTrainings(String userName, String fromDate,
                                       String toDate, String trainerName, TrainingType trainingType) throws Exception;
    public T selectByUsername(String username) throws Exception;
    public void assignTraining(String username,Training training) throws Exception;


}
