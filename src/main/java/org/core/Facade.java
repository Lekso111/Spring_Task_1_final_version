package org.core;


import org.core.Storage.EmbeddedStorage;
import org.core.Utilities.TextParser.UserCSVParser.TraineeParser;
import org.core.config.ConfigClass;
import org.core.services.TrainingServices.TrainingService;
import org.core.services.UserServices.TraineeService;
import org.core.services.UserServices.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Facade {


    public static ApplicationContext context;

    TraineeService traineeService;
    TrainerService trainerService;
    TrainingService trainingService;

    @Autowired
    public Facade(TraineeService traineeService,TrainerService trainerService,TrainingService trainingService){
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }





    public static EmbeddedStorage displayUserStorage(EmbeddedStorage storage){
        return storage;
    }

    public static void main(String[] args) throws Exception {

        ApplicationContext context = new AnnotationConfigApplicationContext(ConfigClass.class);
        TraineeParser parser = new TraineeParser();
        EmbeddedStorage storage = context.getBean(EmbeddedStorage.class);

    }
}