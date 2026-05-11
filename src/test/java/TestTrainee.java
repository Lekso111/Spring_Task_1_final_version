import org.core.Storage.Embedded_Storage;
import org.core.config.ConfigClass;
import org.core.models.Trainee;
import org.core.models.User;
import org.core.services.UserServices.TraineeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class TestTrainee {

    /*

    i wanted to test spring-managed beans,thats why i didnt use new() operator for instantiating objects

     */


    ApplicationContext context = new AnnotationConfigApplicationContext(ConfigClass.class);
    TraineeService service = context.getBean("traineeService",TraineeService.class);
    Map<Integer, User> storage = context.getBean("storage",Embedded_Storage.class).get_storage();
    Trainee trainee = context.getBean("trainee", Trainee.class);

    @Test
    public void testIfTraineeAdded(){
        trainee = new Trainee(
                "John","Jones","ufc123","brutality",true,1980,1,"USA"
        );
        service.addTrainee(trainee);
        assertEquals(storage.get(trainee.userId),trainee);
    }

    @Test
    public void testIfTraineeUpdated(){
        Trainee trainee1 = context.getBean("trainee", Trainee.class);
        service.addTrainee(trainee1);
        //update firstname to 'Mike;
        service.updateTrainee(trainee1,"Mike");
        assertEquals(trainee1.firstName,"Mike");
    }


    @Test
    public void testIfTraineeDeleted(){
        Trainee trainee1 = context.getBean("trainee",Trainee.class);
        service.addTrainee(trainee1);
        testIfTraineeAdded();
        service.deleteTrainee(trainee1);
        assertSame(storage.get(trainee1.userId),null);
    }



    @Test
    public void testIfTraineeSelected(){
        Trainee trainee1 = context.getBean("trainee",Trainee.class);
        int trainee_id = trainee1.userId;
        assertSame(storage.get(trainee_id),service.selectTraineeByUserId(trainee_id));

    }



}
