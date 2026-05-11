package org.core.services.UserServices;


import org.core.dao.UserDaoAbstraction.TraineeDao;
import org.core.models.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service("traineeService")
public class TraineeService {


    static Logger logger = Logger.getLogger("trainee service logger");


    @Autowired
    TraineeDao dao;

    public void addTrainee(Trainee user){
        dao.addUser(user);
    }

    public void updateTrainee(Trainee user,String updatedName){
        dao.updateUser(user,updatedName);
    }
    public void deleteTrainee(Trainee user){
        dao.deleteTrainee(user);
    }
    public Trainee selectTraineeByUserId(int id){
        Trainee selected_trainee = dao.selectUser(id);
        return selected_trainee;
    }


}
