package org.core.dao.TrainingDaoAbstraction;


import org.core.StorageTraining.TrainingStorage;
import org.core.models.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingDao {

    TrainingStorage training_storage;

    @Autowired
    public void setTraining_storage(TrainingStorage training_storage){
        this.training_storage = training_storage;
    }

    public void addTraining(Training training){
        training_storage.addTraining(training);
    }

    public Training selectTraining(Training training){
        return  training_storage.selectTraining(training);
    }


}
