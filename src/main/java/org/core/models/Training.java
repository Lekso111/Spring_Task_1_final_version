package org.core.models;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component("training")
public class Training {


    public  TrainingType trainingType;

    public int traineeId,trainerId;
    public String name;
    public LocalDate date;
    public double duration;




    @Autowired
    public Training(@Value("${training.name}") String name, @Value("${training.date}")String date,@Value("${training.duration}") double duration,
                    @Value("${trainee.userId}") int traineeId,
                    @Value("${trainer.userId}") int trainerId,@Value("${training.type}")TrainingType trainingType){
        this.name = name;
        this.date = LocalDate.parse(date);
        this.duration = duration;
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingType = trainingType;
    }

    public Training(){};


    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public void setTraineeId(int traineeId) {
        this.traineeId = traineeId;
    }

    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Training{" +
                "trainingType=" + trainingType +
                ", traineeId=" + traineeId +
                ", trainerId=" + trainerId +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", duration=" + duration +
                '}';
    }



}
