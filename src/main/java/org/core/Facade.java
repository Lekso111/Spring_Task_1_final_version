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








        TrainingType type = entityManager.find(TrainingType.class,1);

        Training training = context.getBean(Training.class);
        training.setName("rugby");
        training.setDuration(200);
        training.setDate("2020-02-12");
        training.setTrainingType(type);


        Training training2 = context.getBean(Training.class);
        training2.setName("basketball");
        training2.setDuration(200);
        training2.setDate("2020-02-12");
        training2.setTrainingType(type);


        TrainingType type2 = entityManager.find(TrainingType.class,3);


        Training training3 = context.getBean(Training.class);
        training3.setName("armwrestling");
        training3.setDuration(200);
        training3.setDate("2020-02-12");
        training3.setTrainingType(type2);

        trainingService.add(training);
        trainingService.add(training2);
        trainingService.add(training3);



        Trainer trainer = context.getBean(Trainer.class);
        trainer.setFirstName("lekso");
        trainer.setLastName("nanava");
        trainer.setUserName();
        trainer.setPassword();
        trainer.setActiveStatus(true);
//        trainer.setUser();




        trainerService.add(trainer);
        trainerRepo.assignTraining("lekso.nanava1",training3);

        Trainee trainee = context.getBean(Trainee.class);
        trainee.setFirstName("leksusa");
        trainee.setLastName("pots");
        trainee.setUserName();
        trainee.setPassword();
        trainee.setActive(false);
        trainee.setAddress("saburtalo");
//        trainee.setUser();




        traineeService.add(trainee);
        traineeRepo.assignTraining("leksusa.pots1",training3);
        traineeRepo.assignTraining("leksusa.pots1",training);


        System.out.println(trainee.getTrainings());

        System.out.println(trainerService.getTrainings(
                "lekso.nanava1","2020-02-12","2020-02-12","leksusa.pots1",type2
        ));


        trainerService.changePassword("lekso.nanava1", trainer.getPassword(), "axali");
        trainerService.deactivate(trainer,"lekso.nanava1","axali");
        traineeService.getTrainings(
                "leksusa.pots1","2010-05-10","2025-03-19","lekso.nanava1",type2
        ).stream().map(Training::getName).forEach(System.out::println);

        trainerService.changePassword("lekso.nanava1","axali","Programming12345!");

        System.out.println(training.getTrainingType());


        Trainer retrievedTrainer = entityManager.find(Trainer.class,trainer.getId());

        Trainee trainee2  = context.getBean(Trainee.class);
        trainee2.setFirstName("nodo");
        trainee2.setLastName("pots");
        trainee2.setUserName();
        trainee2.setPassword();
//        trainee2.setUser();
        trainee2.setAddress("boch");

        traineeService.add(trainee2);
        traineeService.assignTrainer(trainee2,trainer);
        traineeService.assignTrainer(trainee,trainer);
        trainerService.assignTraining("lekso.nanava1",training);
        traineeService.changePassword("nodo.pots1",trainee2.getPassword(),"ragac");
        traineeService.assignTraining("nodo.pots1",training2);
        trainerService.assignTraining("lekso.nanava1",training2);




        entityManager.getTransaction().begin();
        TrainingType type3 = entityManager.find(TrainingType.class,3);
        training3.setTrainingType(type3);

        Training mergedTraining = entityManager.merge(training3);

        trainerService.getTrainings(
                "lekso.nanava1","2010-02-01","2026-07-03","nodo.pots1",type3
        ).stream().map(Training::getName).forEach(System.out::println);


        entityManager.getTransaction().commit();

        trainerService.assignTraining("lekso.nanava1",training3);
        traineeService.assignTraining("nodo.pots1",training3);

//        traineeService.deleteByUsername("nodo.pots1","ragac");

        Trainer trainer5 = context.getBean(Trainer.class);
        trainer5.setFirstName("zura");
        trainer5.setLastName("nick");
        trainer5.setUserName();
        trainer5.setPassword();
        trainer5.setActiveStatus(true);

        trainerService.add(trainer5);

        trainerService.assignTrainee(trainer5,trainee2);


    }
}