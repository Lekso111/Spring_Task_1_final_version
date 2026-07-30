package org.core.services.TrainingServices;


//import org.core.dao.TrainingDaoAbstraction.TrainingDao;
import org.core.entities.Training;
import org.core.repositories.TrainingRepository.TrainingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.*;

@Service
public class TrainingService {

    static Logger logger = Logger.getLogger("training service logger");

    TrainingRepo trainingRepo;

    @Autowired
    public TrainingService(TrainingRepo trainingRepo){
        this.trainingRepo = trainingRepo;
    }

    public void add(Training training){
        trainingRepo.add(training);
    }

    public Optional<Training> select(Integer id){
        return trainingRepo.selectById(id);
    }






}
