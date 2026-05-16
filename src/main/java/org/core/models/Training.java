package org.core.models;



import java.time.LocalDate;


public class Training {


    private  String trainingType;
    private int traineeId,trainerId;
    private String name;
    private LocalDate date;
    private double duration;



    public Training(String name,String date, double duration,
                    int traineeId,
                    int trainerId,String trainingType){
        this.name = name;
        this.date = LocalDate.parse(date);
        this.duration = duration;
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingType = trainingType;
    }


    public Training(){};




    public void setTrainingType(String trainingType) {
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


    public String getTrainingType() {
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
