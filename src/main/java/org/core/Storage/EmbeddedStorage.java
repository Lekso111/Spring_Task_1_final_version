package org.core.Storage;

import jakarta.annotation.PostConstruct;
import org.core.Utilities.TextParser.UserCSVParser.TraineeParser;
import org.core.Utilities.TextParser.UserCSVParser.TrainerParser;
import org.core.models.Trainee;
import org.core.models.Trainer;
import org.core.models.User;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


@Repository
public class EmbeddedStorage {



    private static Logger logger = Logger.getLogger("storage logger");
    private TraineeParser traineeParser = new TraineeParser();
    private TrainerParser trainerParser = new TrainerParser();
    private Map<Integer, User> storage = new HashMap<>();
    private Map<Integer,Trainee> traineeStorage = new HashMap<>();
    private Map<Integer,Trainer> trainerStorage = new HashMap<>();

    public EmbeddedStorage() throws IOException {
    }


    @PostConstruct
    public void init() throws Exception{
        traineeStorage.putAll(traineeParser.parseCSV());
         trainerStorage.putAll(trainerParser.parseCSV());
    }



    public Map<Integer,Trainee> getTraineeStorage(){
        return traineeStorage;
    }

    public Map<Integer,Trainer> getTrainerStorage(){
        return trainerStorage;
    }

    public void addToTraineeStorage(Trainee trainee){
        if(!(traineeStorage.get(trainee.getUserId())==trainee)){
            traineeStorage.put(trainee.getUserId(),trainee);
            logger.info("Trainee " +traineeStorage.get(trainee.getUserId()).getFirstName()+
                    " with username : ''"+traineeStorage.get(trainee.getUserId()).getUsername()+"'' was added");
        }
    }


    public void updateToTraineeStorage(Trainee trainee,String updatedName){
        try{
            if(!(traineeStorage.get(trainee.getUserId()).equals(null))){
                Trainee updated_user = trainee;
                updated_user.setFirstName(updatedName);

                traineeStorage.replace(trainee.getUserId(),trainee,updated_user);
                logger.info("Trainee " +traineeStorage.get(trainee.getUserId()).getFirstName()+
                        " with username : ''"+traineeStorage.get(trainee.getUserId()).getUsername()+"'' was updated");
            }else{
                throw new NullPointerException();
            }
        }catch(NullPointerException e){
            logger.severe("Trainee " + traineeStorage.get(trainee.getUserId()).getFirstName() + " "+
                    traineeStorage.get(trainee.getUserId()).getLastName()+" with traineeid : " + trainee.getUserId() + " was not found to be updated");
        }
    }

    public void deleteFromTraineeStorage(Trainee trainee){
        try {
            if (!traineeStorage.get(trainee.getUserId()).equals(null)) {
                traineeStorage.remove(trainee.getUserId());
                logger.severe("Trainee " +traineeStorage.get(trainee.getUserId()).getFirstName()+" with username : ''"
                        +traineeStorage.get(trainee.getUserId()).getUsername()+"'' was deleted");
            }else{
                throw new NullPointerException();
            }
        }catch (NullPointerException e){
            logger.severe("Trainee with userid " + trainee.getUserId()+ " was not found in storage");
        }
    }

    public Trainee selectFromTraineeStorage(int id){
        return (Trainee) traineeStorage.get(id);
    }



    public void addToTrainerStorage(Trainer trainer){
        trainerStorage.put(trainer.getUserId(),trainer);
    }


    public void updateToTrainerStorage(Trainer trainer,String newName){
        try{
            if(!trainerStorage.get(trainer.getUserId()).equals(null)){
                Trainer updated_user = trainer;
                updated_user.setFirstName(newName);

                trainerStorage.replace(trainer.getUserId(),trainer,updated_user);
                logger.info("Trainer " +trainerStorage.get(trainer.getUserId()).getFirstName()+
                        " with username : ''"+trainerStorage.get(trainer.getUserId()).getUsername()+"'' was updated");
            }else{
                throw new NullPointerException();
            }
        }catch(NullPointerException e){
            logger.severe("Trainer " + trainerStorage.get(trainer.getUserId()).getFirstName() + " "+
                    trainerStorage.get(trainer.getUserId()).getLastName()+" with trainerid : " + trainer.getUserId() + " was not found to be updated");
        }
    }


    public Trainer selectFromTrainerStorage(int id){
        return (Trainer) trainerStorage.get(id);
    }

    @Override
    public String toString(){
        return storage.toString();
    }


}
