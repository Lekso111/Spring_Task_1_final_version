package org.core.StorageTraining;


import jakarta.annotation.PostConstruct;
import org.core.models.Training;
import org.core.models.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component("trainingStorage")
public class TrainingStorage {


    @Autowired
    Environment environment;


    private static String storage_displayed = "{";

    private Map<String, Training> trainingStorage = new HashMap<>();


    @PostConstruct
    public void init(){
        Training training = new Training();
        TrainingType training_type = new TrainingType(environment.getProperty("training.type"));

        training.setTraineeId(Integer.parseInt(environment.getProperty("trainee.userId")));
        training.setTrainerId(Integer.parseInt(environment.getProperty("trainer.userId")));
        training.setName(environment.getProperty("training.name"));
        training.setTrainingType(training_type);
        training.setDate(LocalDate.parse(environment.getProperty("training.date")));
        training.setDuration(Double.parseDouble(environment.getProperty("training.duration")));
        trainingStorage.put(training.name,training);
    }


    public Map<String,Training> get_training_storage(){
        return trainingStorage;
    }


    public void addTraining(Training training){
        trainingStorage.put(training.name,training);

    }

    public Training selectTraining(Training training){
        return trainingStorage.get(training.name);
    }

    @Override
    public String toString(){
        for(Map.Entry<String,Training> entry : get_training_storage().entrySet()){
            storage_displayed = storage_displayed + entry.getValue().toString()+";";
        }

        return storage_displayed + "}";
    }


}
