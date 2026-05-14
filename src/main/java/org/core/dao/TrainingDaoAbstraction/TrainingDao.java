package org.core.dao.TrainingDaoAbstraction;


import org.core.StorageTraining.TrainingStorage;
import org.core.models.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingDao {

    TrainingStorage trainingStorage;

    @Autowired
    public TrainingDao(TrainingStorage storage){
        this.trainingStorage = storage;
    }



    public void add(Training training){
        trainingStorage.add(training);
    }

    public Training select(Training training){
        return  trainingStorage.select(training);
    }


}
