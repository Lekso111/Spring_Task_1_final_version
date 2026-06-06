import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.core.config.ConfigClass;
import org.core.entities.*;
import org.core.services.TrainingServices.TrainingService;
import org.core.services.UserServices.TraineeService;
import org.core.services.UserServices.TrainerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.*;
import static org.testng.AssertJUnit.assertEquals;

@Test
public class TrainerTest {

    EntityManager entityManager = Persistence.createEntityManagerFactory("myPersistenceUnit").createEntityManager();
    ApplicationContext context =
            new AnnotationConfigApplicationContext(ConfigClass.class);
    TrainerService trainerService = context.getBean(TrainerService.class);




    @Test
    public void testTrainerAdded(){

        Trainer trainer = new Trainer();
        trainer.setFirstName("lekso");
        trainer.setLastName("potskhverashvili");
        trainer.setUserName();
        trainer.setPassword();


        entityManager.getTransaction().begin();
        entityManager.persist(trainer);
        entityManager.getTransaction().commit();

        Trainer retrievedTrainer = entityManager.find(Trainer.class,trainer.getId());
        assertSame(trainer,retrievedTrainer);

    }




    @Test
    public void testTrainerActivated() throws Exception {
        Trainer trainer = new Trainer();
        trainer.setFirstName("lekso");
        trainer.setLastName("potskhverashvili");
        trainer.setUserName();
        trainer.setPassword();
        trainer.setActiveStatus(false);


        trainerService.add(trainer);


        trainerService.activate(trainer,trainer.getUserName(),trainer.getPassword());
        Users retrievedTrainer = entityManager.find(Users.class,trainer.getId());
        assertTrue(retrievedTrainer.isActive());
    }



    @Test
    public void testTrainerDeactivated() throws Exception {
        Trainer trainer = new Trainer();
        trainer.setFirstName("lekso");
        trainer.setLastName("potskhverashvili");
        trainer.setUserName();
        trainer.setPassword();
        trainer.setActiveStatus(true);


        trainerService.add(trainer);


        trainerService.deactivate(trainer,trainer.getUserName(),trainer.getPassword());
        Users retrievedTrainer = entityManager.find(Users.class,trainer.getId());
        assertFalse(retrievedTrainer.isActive());

    }


    @Test
    public void testTrainerPasswordChanged() throws Exception{
        Trainer trainer = new Trainer();
        trainer.setFirstName("lekso");
        trainer.setLastName("potskhverashvili");
        trainer.setUserName();
        trainer.setPassword();
        trainer.setActiveStatus(true);


        String oldPassword = trainer.getPassword();
        String username = trainer.getUserName();

        trainerService.add(trainer);
        trainerService.changePassword(username,oldPassword,"axali paroli");

        Trainer retrievedTrainer = entityManager.find(Trainer.class,trainer.getId());
        assertEquals("axali paroli",retrievedTrainer.getUser().getPassword());
    }




    @Test
    public void testTrainingAssigned() throws Exception{

        TrainingType trainingType = entityManager.find(TrainingType.class,2);
        TrainingService trainingService = context.getBean(TrainingService.class);
        TraineeService traineeService = context.getBean(TraineeService.class);

        Training training = context.getBean(Training.class);
        training.setTrainingType(trainingType);
        training.setName("rugby");
        training.setDuration(200);
        training.setDate("2025-02-01");
        trainingService.add(training);


        Trainee trainee = context.getBean(Trainee.class);

        trainee.setFirstName("michael");
        trainee.setLastName("smith");
        trainee.setUserName();
        trainee.setPassword();
        trainee.setActive(true);



        traineeService.add(trainee);
        traineeService.assignTraining("michael.smith1",training);

        Trainer trainer = new Trainer();

        trainer.setFirstName("lekso");
        trainer.setLastName("potskhverashvili");
        trainer.setUserName();
        trainer.setPassword();
        trainer.setActiveStatus(true);



        trainerService.add(trainer);
        trainerService.assignTraining("lekso.potskhverashvili1",training);
        Training retrievedTraining = entityManager.find(Training.class,training.getId());


        assertEquals(retrievedTraining.getId(),trainerService.getTrainings(
                trainer.getUserName(), "2010-10-12","2026-02-01",
                trainee.getUserName(),trainingType
        ).get(0).getId());

    }


}
