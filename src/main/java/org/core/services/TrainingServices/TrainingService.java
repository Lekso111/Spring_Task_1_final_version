package org.core.services.TrainingServices;


import org.core.dao.TrainingDaoAbstraction.TrainingDao;
import org.core.models.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.logging.*;

@Service
public class TrainingService {

    static Logger logger = Logger.getLogger("training service logger");
    private TrainingDao trainingDao;


    @Autowired
    public TrainingService(TrainingDao dao){
        this.trainingDao = dao;
    }



     public void add(Training training){
         trainingDao.add(training);
         logger.info("training profile was added");
     }

     public Training select(Training training){
         logger.info("Training profile was selected");
         return trainingDao.select(training);
     }






}
