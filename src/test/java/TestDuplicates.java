import org.core.Storage.Embedded_Storage;
import org.core.config.ConfigClass;
import org.core.models.Trainee;
import org.core.models.Trainer;
import org.core.services.UserServices.TraineeService;
import org.core.services.UserServices.TrainerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestDuplicates {

    ApplicationContext context = new AnnotationConfigApplicationContext(ConfigClass.class);
    TraineeService trainee_service = context.getBean("traineeService",TraineeService.class);
    TrainerService trainer_service = context.getBean("trainerService", TrainerService.class);
    Trainee trainee = context.getBean("trainee", Trainee.class);
    Trainer trainer = context.getBean("trainer",Trainer.class);

    Embedded_Storage storage = context.getBean("storage", Embedded_Storage.class);


    @Test
    public void checkIfTraineesWithSameIdsExist(){
        trainee = new Trainee(
                "lekso","potskhverashvili","lekso111","ragac",
                false,2004,200,"tbilisi"
        );
        trainee_service.addTrainee(trainee);
        Trainee trainee1 = context.getBean("trainee",Trainee.class);
        trainee1 = new Trainee(
                "kevin","mitnick","ethical hacker","systems",true,
                1980,200,"england"
        );
        trainee_service.addTrainee(trainee1);
        System.out.println(storage);
        assertFalse((storage.get_storage().get(200)).firstName == trainee.firstName,
                "this boolean condition verifies that when we add two trainees with same ids,latest added one will overwrite the first one" +
                        "and we will have only one record with given id key");

    }





}
