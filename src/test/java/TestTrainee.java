import org.core.Storage.EmbeddedStorage;
import org.core.config.ConfigClass;
import org.core.models.Trainee;
import org.core.models.User;
import org.core.services.UserServices.TraineeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class TestTrainee {



    ApplicationContext context = new AnnotationConfigApplicationContext(ConfigClass.class);
    TraineeService service = context.getBean("traineeService",TraineeService.class);
    Map<Integer, Trainee> storage = context.getBean(EmbeddedStorage.class).getTraineeStorage();
    Trainee trainee = context.getBean("trainee", Trainee.class);

    @Test
    public void testIfTraineeAdded(){
        trainee = new Trainee(
                "John","Jones","ufc123","brutality",true,1980,1,"USA"
        );
        service.add(trainee);
        assertEquals(storage.get(trainee.getUserId()),trainee);
    }

    @Test
    public void testIfTraineeUpdated(){
        Trainee trainee1 = context.getBean("trainee", Trainee.class);
        service.add(trainee1);
        //update firstname to 'Mike;
        service.update(trainee1,"Mike");
        assertEquals(trainee1.getFirstName(),"Mike");
    }


    @Test
    public void testIfTraineeDeleted(){
        Trainee trainee1 = context.getBean("trainee",Trainee.class);
        service.add(trainee1);
        testIfTraineeAdded();
        service.delete(trainee1);
        assertSame(storage.get(trainee1.getUserId()),null);
    }



    @Test
    public void testIfTraineeSelected(){
        Trainee trainee1 = context.getBean("trainee",Trainee.class);
        int trainee_id = trainee1.getUserId();
        assertSame(storage.get(trainee_id),service.selectByUserId(trainee_id));

    }



}
