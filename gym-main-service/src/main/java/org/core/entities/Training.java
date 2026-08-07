package org.core.entities;



import jakarta.persistence.*;


import java.io.Serializable;
import java.time.LocalDate;


@Entity
public class Training{




    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;


    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private double duration;



    @ManyToOne
    @JoinColumn(name="traineeId")
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name="trainerId")
    private Trainer trainer;


    @ManyToOne
    @JoinColumn(name="trainingTypeId")
    private TrainingType trainingType;











    public Training(String name,String date, double duration){
        this.name = name;
        this.date = LocalDate.parse(date);
        this.duration = duration;
    }




    public Training(){};


    public void setName(String name) {
        this.name = name;
    }
    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }
    public void setDate(String date) {
        this.date = LocalDate.parse(date);
    }
    public void setDuration(double duration) {
        this.duration = duration;
    }
    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;

    }





    public Integer getId() {
        return id;
    }
    public LocalDate getDate() {
        return date;
    }
    public double getDuration() {
        return duration;
    }
    public String getName() {
        return name;
    }
    public Trainee getTrainee() {
        return trainee;
    }
    public Trainer getTrainer() {
        return trainer;
    }
    public TrainingType getTrainingType() {
        return trainingType;
    }












}
