import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.core.config.ConfigClass;
import org.core.entities.Training;
import org.core.entities.TrainingType;
import org.core.services.TrainingServices.TrainingService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

@Test
public class TrainingTest {

    EntityManager entityManager = Persistence.createEntityManagerFactory("myPersistenceUnit").createEntityManager();
    ApplicationContext context =
            new AnnotationConfigApplicationContext(ConfigClass.class);
    TrainingService trainingService = context.getBean(TrainingService.class);

    @Test
    public void testTrainingAdded(){
        TrainingType trainingType = entityManager.find(TrainingType.class,2);
        Training training = context.getBean(Training.class);
        training.setName("boxing");
        training.setTrainingType(trainingType);
        training.setDuration(400);
        training.setDate("2020-05-01");

        trainingService.add(training);
        Training retrievedTraining = entityManager.find(Training.class,training.getId());
        assertEquals("boxing",retrievedTraining.getName());
    }




}
