package org.core.Storage;

import jakarta.annotation.PostConstruct;
import org.core.models.Trainee;
import org.core.models.Trainer;
import org.core.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component("storage")
public class Embedded_Storage {


    @Autowired
    Environment environment;
    private Map<Integer, User> storage = new HashMap<>();


    @PostConstruct
    public void init(){

        Trainee trainee = new Trainee();

        trainee.setFirstName(environment.getProperty("trainee.name"));
        trainee.setLastName(environment.getProperty("trainee.lastname"));
        trainee.setUsername(environment.getProperty("trainee.username"));
        trainee.setPassword(environment.getProperty("trainee.password"));
        trainee.setActive(Boolean.parseBoolean(environment.getProperty("trainee.active")));
        trainee.setDate_of_birth(Integer.parseInt(environment.getProperty("trainee.birth")));
        trainee.setUserId(Integer.parseInt(environment.getProperty("trainee.userId")));
        trainee.setAddress(environment.getProperty("trainee.address"));

        storage.put(trainee.userId, trainee);



        Trainer trainer = new Trainer();

        trainer.setFirstName(environment.getProperty("trainer.firstname"));
        trainer.setLastName(environment.getProperty("trainer.lastname"));
        trainer.setUsername(environment.getProperty("trainer.username"));
        trainer.setPassword(environment.getProperty("trainer.password"));
        trainer.setActive(Boolean.parseBoolean(environment.getProperty("trainer.active")));
        trainer.setSpecialisation(environment.getProperty("trainer.specialisation"));
        trainer.setUserId(Integer.parseInt(environment.getProperty("trainer.userId")));

        storage.put(trainer.userId, trainer);
    }


    public Map<Integer,User> get_storage(){
        return storage;
    }

    @Override
    public String toString(){
        return storage.toString();
    }


}
