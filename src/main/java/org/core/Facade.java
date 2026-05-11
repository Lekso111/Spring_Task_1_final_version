package org.core;


import org.core.Storage.Embedded_Storage;
import org.core.StorageTraining.TrainingStorage;
import org.core.Utilities.PasswordGeneration;
import org.core.Utilities.UsernameGeneration;
import org.core.config.ConfigClass;
import org.core.dao.UserDaoAbstraction.TraineeDao;
import org.core.dao.UserDaoAbstraction.TrainerDao;
import org.core.models.*;
import org.core.services.TrainingServices.TrainingService;
import org.core.services.UserServices.TraineeService;
import org.core.services.UserServices.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.logging.*;



public class Facade {



    TraineeService traineeService;
    TrainerService trainerService;
    TrainingService trainingService;

    @Autowired
    public Facade(TraineeService traineeService,TrainerService trainerService,TrainingService trainingService){
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }





    public static Embedded_Storage displayUserStorage(Embedded_Storage storage){
        return storage;
    }



    public static void main(String[] args) {


        ApplicationContext context = new AnnotationConfigApplicationContext(ConfigClass.class);
        Embedded_Storage storage = context.getBean("storage", Embedded_Storage.class);
//        Trainer trainer = context.getBean("trainer",Trainer.class);
//        TrainerService trainerService1 = context.getBean("trainerService",TrainerService.class);
//
//        trainerService1.addTrainer(trainer);
//
//        System.out.println(displayUserStorage(storage));


    }


}