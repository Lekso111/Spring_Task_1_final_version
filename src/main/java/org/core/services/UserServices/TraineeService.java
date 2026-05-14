package org.core.services.UserServices;


import org.core.dao.UserDaoAbstraction.TraineeDao;
import org.core.models.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class TraineeService {


    static Logger logger = Logger.getLogger("trainee service logger");



    TraineeDao dao;

    @Autowired
    public TraineeService(TraineeDao dao){
        this.dao = dao;
    }


    public void add(Trainee user){
        dao.add(user);
    }
    public void update(Trainee user, String updatedName){
        dao.update(user,updatedName);
    }
    public void delete(Trainee user){
        dao.delete(user);
    }
    public Trainee selectByUserId(int id){
        Trainee selected_trainee = dao.select(id);
        return selected_trainee;
    }


}
