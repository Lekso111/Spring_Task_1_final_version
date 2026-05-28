package org.core.dao.UserDaoAbstraction;


import org.core.Storage.EmbeddedStorage;
import org.core.models.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.logging.Logger;


@Repository
public class TraineeDao implements UserDao<Trainee>{

    public static Logger logger = Logger.getLogger("traineeDao logger");


    private EmbeddedStorage storage;

    @Autowired
   public TraineeDao(EmbeddedStorage storage){
        this.storage = storage;
    }




    @Override
    public void add(Trainee user) {
       storage.addToTraineeStorage(user);
    }


    @Override
    public void update(Trainee user, String updatedName) {
        storage.updateToTraineeStorage(user,updatedName);
    }

    @Override
    public Trainee select(int id) {
        return storage.selectFromTraineeStorage(id);
    }


    public void delete(Trainee user) {
      storage.deleteFromTraineeStorage(user);
    }





}
