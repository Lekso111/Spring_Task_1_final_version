import org.core.StorageTraining.TrainingStorage;
import org.core.config.ConfigClass;
import org.core.models.Training;
import org.core.models.TrainingType;
import org.core.services.TrainingServices.TrainingService;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import static org.junit.jupiter.api.Assertions.*;


public class TestTraining {

    ApplicationContext context = new AnnotationConfigApplicationContext(ConfigClass.class);
    Training training = context.getBean("training",Training.class);
    TrainingType trainingType = context.getBean("trainingType",TrainingType.class);
    TrainingStorage storage = context.getBean("trainingStorage",TrainingStorage.class);
    TrainingService service = context.getBean("trainingService",TrainingService.class);

    @Test
    public void testIfTrainingAdded(){
        service.add(training);
        String training_name = training.getName();
        assertNotSame(storage.getTrainingStorage().get(training_name),null);
    }



    @Test
    public void testIfTrainingSelected(){
        String training_name = training.getName();


        assertEquals(training_name,service.select(training).getName());
    }



}
