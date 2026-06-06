package org.core.services.UserServices;

import org.core.entities.Training;
import org.core.entities.TrainingType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface UserServiceAbstraction<T> {

    public void add(T user);
    public void activate(T updateable,String username,String password) throws  Exception;
    public void deactivate(T updateable,String username,String password) throws Exception;
    public Optional<T> select(String username,String password) throws Exception;
    public void changePassword(String username,String oldPassword,String newPassword) throws Exception;
    public void assignTraining(String username,Training training) throws Exception;
    public List<Training> getTrainings(String username, String fromDate,
                                       String toDate, String name, TrainingType trainingType) throws Exception;

}
