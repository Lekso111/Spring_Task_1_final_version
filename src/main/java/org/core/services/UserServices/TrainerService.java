package org.core.services.UserServices;



import org.core.entities.*;
import org.core.repositories.UserRepository.TrainerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class TrainerService implements UserServiceAbstraction<Trainer>{

    public static Logger logger = Logger.getLogger("Trainer Service logger");


    private TrainerRepo trainerRepo;
    @Autowired
    public TrainerService(TrainerRepo trainerRepo){
        this.trainerRepo = trainerRepo;
    }


    @Override
    public void add(Trainer user) {
        trainerRepo.add(user);
    }

    @Override
    public void activate(Trainer trainer, String username, String password) throws Exception {
        Users updateable = trainer.getUser();
        if(!updateable.isActive()){
            trainerRepo.update(trainer,username,password,true);
            logger.severe("Trainer " + trainer.getUser().getUserName() + " has been successfully activated");
        }else{
            logger.severe("You already have following Trainer instance activated");
        }
    }

    @Override
    public void deactivate(Trainer trainer, String username, String password) throws Exception {

        Users updateable = trainer.getUser();
        if(updateable.isActive()){
            trainerRepo.update(trainer,username,password,false);
            logger.severe("Trainer " + trainer.getUser().getUserName() + " has been successfully deactivated");
        }else{
            logger.severe("You already have following Trainer instance deactivated");
        }
    }

    @Override
    public Optional<Trainer> select(String username, String password) throws Exception {
        return trainerRepo.select(username,password);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) throws Exception {
        trainerRepo.updatePassword(username,oldPassword,newPassword);
    }

    @Override
    public void assignTraining(String username, Training training) throws Exception {
        trainerRepo.assignTraining(username,training);
    }

    @Override
    public List<Training> getTrainings(String username, String fromDate,
                                       String toDate, String name, TrainingType trainingType) throws Exception {

        return trainerRepo.getTrainings(username,fromDate,toDate,name,trainingType);
    }


    public void assignTrainee(Trainer trainer, Trainee trainee){
        trainerRepo.assignTrainee(trainer,trainee);
    }


}
