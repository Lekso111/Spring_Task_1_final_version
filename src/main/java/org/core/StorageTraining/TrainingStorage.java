package org.core.StorageTraining;


import jakarta.annotation.PostConstruct;
import org.core.models.Training;
import org.core.models.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TrainingStorage {



    public ApplicationContext context;
    private Training training;
    private Map<String, Training> trainingStorage = new HashMap<>();


    @Autowired
    public TrainingStorage(ApplicationContext context){
        this.context = context;
    }

    @PostConstruct
    public void init(){
        Training training = context.getBean("training", Training.class);
        trainingStorage.put(training.getName(),training);

    }




    public void add(Training training){
        trainingStorage.put(training.getName(),training);
    }

    public Training select(Training training){
        return trainingStorage.get(training.getName());
    }

    public Map<String,Training> getTrainingStorage(){
        return trainingStorage;
    }


}
