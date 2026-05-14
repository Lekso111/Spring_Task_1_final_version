package org.core.dao.UserDaoAbstraction;


import jakarta.annotation.PostConstruct;
import org.core.Storage.EmbeddedStorage;
import org.core.models.Trainer;
import org.core.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.logging.Logger;

@Repository
public class TrainerDao implements UserDao<Trainer>{

    public static Logger logger = Logger.getLogger("Trainerdao logger");


    EmbeddedStorage storage;

    @Autowired
    public TrainerDao(EmbeddedStorage storage){
        this.storage = storage;
    }

    Map<Integer, Trainer> retrieved_storage;


//    @PostConstruct
//    public void init(){
//        retrieved_storage = storage.getTrainerStorage();
//    }

    @Override
    public void add(Trainer user) {
            storage.addToTrainerStorage(user);
    }

    @Override
    public void update(Trainer user, String updatedName) {
        storage.updateToTrainerStorage(user,updatedName);
    }

    @Override
    public Trainer select(int id) {
        return (Trainer) storage.selectFromTrainerStorage(id);
    }
}
