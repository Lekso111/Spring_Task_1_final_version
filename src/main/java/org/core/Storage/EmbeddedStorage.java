package org.core.Storage;

import jakarta.annotation.PostConstruct;
import org.core.Utilities.TextFileParser;
import org.core.models.Trainee;
import org.core.models.Trainer;
import org.core.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


@Component
public class EmbeddedStorage {


    ApplicationContext context;
    private static Logger logger = Logger.getLogger("storage logger");
    private TextFileParser fileParser = new TextFileParser("src/main/resources/initial-data.txt");
    private Map<String,String> parsedMap = fileParser.getParsedFile();
    private Trainer trainer;
    private Trainee trainee;
    private Map<Integer, User> storage = new HashMap<>();
    private Map<Integer,Trainee> traineeStorage = new HashMap<>();
    private Map<Integer,Trainer> trainerStorage = new HashMap<>();


    @Autowired
     public void setTrainer(Trainer trainer){
         this.trainer = trainer;
     }


    @PostConstruct
    public void init(){
        trainee = context.getBean("trainee",Trainee.class);
        traineeStorage.put(trainee.getUserId(),trainee);
        trainer = context.getBean("trainer",Trainer.class);
        trainerStorage.put(trainer.getUserId(),trainer);
    }








    @Override
    public String toString(){
        return storage.toString();
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



    @Autowired
    public void setApplicationContext(ApplicationContext context){
        this.context = context;
    }





}
