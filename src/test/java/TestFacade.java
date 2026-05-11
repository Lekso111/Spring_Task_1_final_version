import org.core.Facade;
import org.core.Storage.Embedded_Storage;
import org.core.config.ConfigClass;
import org.core.services.TrainingServices.TrainingService;
import org.core.services.UserServices.TraineeService;
import org.core.services.UserServices.TrainerService;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

public class TestFacade {

    ApplicationContext context = new AnnotationConfigApplicationContext(ConfigClass.class);
    Embedded_Storage storage = context.getBean("storage", Embedded_Storage.class);
    TrainerService trainer_service = context.getBean("trainerService",TrainerService.class);
    TraineeService trainee_service = context.getBean("traineeService",TraineeService.class);
    TrainingService training_service = context.getBean("trainingService", TrainingService.class);

    Facade facade = new Facade(trainee_service,trainer_service,training_service);


    @Test
    public void testDisplayStorageMethod(){
        assertSame(storage,Facade.displayUserStorage(storage));
    }

}
