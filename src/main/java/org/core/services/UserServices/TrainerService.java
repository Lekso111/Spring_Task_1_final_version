package org.core.services.UserServices;


import org.core.dao.UserDaoAbstraction.TrainerDao;
import org.core.models.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class TrainerService {

    public static Logger logger = Logger.getLogger("Trainer Service logger");


    private TrainerDao dao;


    @Autowired
    public TrainerService(TrainerDao dao) {
        this.dao = dao;
    }

    public void add(Trainer trainer){
        dao.add(trainer);
    }


    public void update(Trainer trainer, String updatedName){
        dao.update(trainer,updatedName);
    }

    public Trainer selectByUserId(int id){
        return dao.select(id);
    }


}
