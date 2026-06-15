import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.core.config.ConfigClass;
import org.core.entities.Trainee;
import org.core.entities.User;
import org.core.services.UserServices.TraineeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.*;

@Test
public class TraineeTest {


    EntityManager entityManager = Persistence.createEntityManagerFactory("myPersistenceUnit").createEntityManager();
    ApplicationContext context =
            new AnnotationConfigApplicationContext(ConfigClass.class);
    TraineeService traineeService = context.getBean(TraineeService.class);




    @Test
    public void testTraineeAdded(){
        Trainee trainee = new Trainee();
        trainee.setFirstName("lekso");
        trainee.setLastName("potskhverashvili");
        trainee.setUserName();
        trainee.setPassword();
        trainee.setAddress("saburtalo");
        trainee.setDateOfBirth("2004-02-12");
        trainee.setActive(false);


        entityManager.getTransaction().begin();
        entityManager.persist(trainee);
        entityManager.getTransaction().commit();

        Trainee retrievedTrainee = entityManager.find(Trainee.class,trainee.getId());
        assertSame(trainee,retrievedTrainee);

    }




    @Test
    public void testTraineeActivated() throws Exception {


        Trainee trainee = new Trainee();
        trainee.setFirstName("lekso");
        trainee.setLastName("potskhverashvili");
        trainee.setUserName();
        trainee.setPassword();
        trainee.setAddress("saburtalo");
        trainee.setDateOfBirth("2004-02-12");
        trainee.setActive(false);


        traineeService.add(trainee);


        traineeService.activate(trainee,trainee.getUserName(),trainee.getPassword());
        User retrievedTrainee = entityManager.find(User.class,trainee.getId());
        assertTrue(retrievedTrainee.isActive());

    }



    @Test
    public void testTraineeDeactivated() throws Exception {


        Trainee trainee = new Trainee();
        trainee.setFirstName("lekso");
        trainee.setLastName("potskhverashvili");
        trainee.setUserName();
        trainee.setPassword();
        trainee.setAddress("saburtalo");
        trainee.setDateOfBirth("2004-02-12");
        trainee.setActive(true);


        traineeService.add(trainee);


        traineeService.deactivate(trainee,trainee.getUserName(),trainee.getPassword());
        User retrievedTrainee = entityManager.find(User.class,trainee.getId());
        assertFalse(retrievedTrainee.isActive());

    }


    @Test
    public void testTraineePasswordChanged() throws Exception{
        Trainee trainee = new Trainee();
        trainee.setFirstName("lekso");
        trainee.setLastName("potskhverashvili");
        trainee.setUserName();
        trainee.setPassword();
        trainee.setAddress("saburtalo");
        trainee.setDateOfBirth("2004-02-12");
        trainee.setActive(true);


        String oldPassword = trainee.getPassword();
        String username = trainee.getUserName();

        traineeService.add(trainee);
        traineeService.changePassword(username,oldPassword,"axali paroli");

        Trainee retrievedTrainee = entityManager.find(Trainee.class,trainee.getId());
        assertEquals("axali paroli",retrievedTrainee.getUser().getPassword());
    }


    @Test
    public void testTraineeDeleted(){
        Trainee trainee = new Trainee();
        trainee.setFirstName("lekso");
        trainee.setLastName("potskhverashvili");
        trainee.setUserName();
        trainee.setPassword();
        trainee.setAddress("saburtalo");
        trainee.setDateOfBirth("2004-02-12");
        trainee.setActive(true);


        traineeService.add(trainee);
        traineeService.deleteByUsername(trainee.getUserName(),trainee.getPassword());

        boolean traineeDeletedPredicate = entityManager.contains(trainee);

        assertFalse(traineeDeletedPredicate);

    }




}
