package org.core.models;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Training {


    private  TrainingType trainingType;
    private int traineeId,trainerId;
    private String name;
    private LocalDate date;
    private double duration;


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


    public TrainingType getTrainingType() {
        return trainingType;
    }

    public int getTraineeId() {
        return traineeId;
    }

    public int getTrainerId() {
        return trainerId;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getDuration() {
        return duration;
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
