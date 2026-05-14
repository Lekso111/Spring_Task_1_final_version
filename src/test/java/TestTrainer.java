import org.core.Storage.EmbeddedStorage;
import org.core.config.ConfigClass;
import org.core.models.Trainer;
import org.core.models.User;
import org.core.services.UserServices.TrainerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class TestTrainer {


    ApplicationContext context = new AnnotationConfigApplicationContext(ConfigClass.class);
    TrainerService service = context.getBean("trainerService",TrainerService.class);
    Map<Integer, Trainer> storage = context.getBean(EmbeddedStorage.class).getTrainerStorage();
    Trainer trainer = context.getBean("trainer", Trainer.class);



    @Test
    public void testIfTrainerAdded(){
        trainer = new Trainer(
                "James","Tyson","box123","brutality",true,"boxing",2
        );
        service.add(trainer);
        assertEquals(storage.get(trainer.userId),trainer);
    }


    @Test
    public void testIfTrainerUpdated(){
        Trainer trainer1 = context.getBean("trainer", Trainer.class);
        service.add(trainer1);
        //update firstname to 'Mike;
        service.update(trainer1,"Mike");
        assertEquals(trainer1.getFirstName(),"Mike");
    }


    @Test
    public void testIfTrainerSelected(){
        Trainer trainer1 = context.getBean("trainer",Trainer.class);
        int trainer_id = trainer1.userId;
        assertSame(storage.get(trainer_id),service.selectByUserId(trainer_id));

    }
}
