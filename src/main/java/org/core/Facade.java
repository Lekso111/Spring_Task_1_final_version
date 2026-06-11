package org.core;


import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.core.config.ConfigClass;
import org.core.entities.*;
import org.core.repositories.UserRepository.TraineeRepo;
import org.core.repositories.UserRepository.TrainerRepo;
import org.core.services.TrainingServices.TrainingService;
import org.core.services.UserServices.TraineeService;
import org.core.services.UserServices.TrainerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Facade {


    public static ApplicationContext context;


    public static void main(String[] args) throws Exception {


        ApplicationContext context = new AnnotationConfigApplicationContext(ConfigClass.class);
        EntityManager entityManager = Persistence.createEntityManagerFactory("myPersistenceUnit").createEntityManager();
        TraineeRepo traineeRepo = context.getBean(TraineeRepo.class);
        TrainerRepo trainerRepo = context.getBean(TrainerRepo.class);
        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);
        TrainingService trainingService = context.getBean(TrainingService.class);



    }
}