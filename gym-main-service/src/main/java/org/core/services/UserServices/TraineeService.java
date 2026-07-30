package org.core.services.UserServices;
import org.core.entities.*;
import org.core.repositories.UserRepository.TraineeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;



@Service
public class TraineeService implements UserServiceInterface<Trainee> {


    static Logger logger = Logger.getLogger("trainee service logger");
    private TraineeRepo traineeRepo;



    @Autowired
    public TraineeService(TraineeRepo traineeRepo){
        this.traineeRepo = traineeRepo;
    }




    public void add(Trainee trainee){
        traineeRepo.add(trainee);
    }

    @Override
    public void activate(Trainee trainee, String username, String password) throws Exception {
        User updateable = trainee.getUser();
        if(!updateable.isActive()){
            traineeRepo.update(trainee,username,password,true);
            logger.severe("Trainee " + trainee.getUser().getUserName() + " has been successfully activated");
        }else{
            logger.severe("Following Trainee instance is already activated");
        }
    }



    @Override
    public void deactivate(Trainee trainee, String username, String password) throws Exception {
        User updateable = trainee.getUser();
        if(updateable.isActive()){
            traineeRepo.update(trainee,username,password,false);
            logger.severe("Trainee " + updateable.getUserName() + " has been successfully activated");
        }else{
            logger.severe("Following Trainee is already deactivated");
        }
    }

    @Override
    public Optional<Trainee> select(String username, String password) throws Exception {
        return traineeRepo.select(username,password);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) throws Exception {
        traineeRepo.updatePassword(username,oldPassword,newPassword);
    }



    public void assignTraining(String username,Training training) throws Exception{
        traineeRepo.assignTraining(username,training);
    }


    @Override
    public List<Training> getTrainings(String username, String fromDate,
                                       String toDate, String trainerName,
                                       TrainingType trainingType) throws Exception {
        return traineeRepo.getTrainings(username,fromDate,toDate,trainerName,trainingType);
    }



    public void assignTrainer(Trainee trainee,Trainer trainer){
        traineeRepo.assignTrainer(trainee,trainer);
    }


    public void deleteByUsername(String username,String password){
        traineeRepo.deleteByUsername(username,password);
    }




}
