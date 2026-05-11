package org.core.services.UserServices;


import org.core.dao.UserDaoAbstraction.TrainerDao;
import org.core.models.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service("trainerService")
public class TrainerService {

    public static Logger logger = Logger.getLogger("Trainer Service logger");

    @Autowired
    TrainerDao trainerDao;


    public TrainerService(TrainerDao dao) {
        this.trainerDao = dao;
    }

    public void addTrainer(Trainer trainer){
        trainerDao.addUser(trainer);
    }


    public void updateTrainer(Trainer trainer,String updatedName){
        trainerDao.updateUser(trainer,updatedName);

    }

    public Trainer selectTrainerById(int id){
        return trainerDao.selectUser(id);
    }


}
