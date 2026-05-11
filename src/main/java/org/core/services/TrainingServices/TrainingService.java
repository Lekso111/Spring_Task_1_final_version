package org.core.services.TrainingServices;


import org.core.dao.TrainingDaoAbstraction.TrainingDao;
import org.core.models.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.logging.*;

@Service("trainingService")
public class TrainingService {

    static Logger logger = Logger.getLogger("training service logger");


    @Autowired
    TrainingDao trainingDao;



     public void addTrainingProfile(Training training){
         trainingDao.addTraining(training);
         logger.info("training profile was added");
     }

     public Training selectTrainingProfile(Training training){
         logger.info("Training profile was selected");
         return trainingDao.selectTraining(training);
     }






}
