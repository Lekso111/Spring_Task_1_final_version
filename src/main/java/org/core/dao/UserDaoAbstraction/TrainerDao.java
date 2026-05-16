package org.core.dao.UserDaoAbstraction;



import org.core.Storage.EmbeddedStorage;
import org.core.models.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.logging.Logger;

@Repository
public class TrainerDao implements UserDao<Trainer>{

    public static Logger logger = Logger.getLogger("Trainerdao logger");
    private EmbeddedStorage storage;

    @Autowired
    public TrainerDao(EmbeddedStorage storage){
        this.storage = storage;
    }



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
