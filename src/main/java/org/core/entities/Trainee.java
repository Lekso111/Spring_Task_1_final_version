package org.core.entities;
import jakarta.persistence.*;


import java.time.LocalDate;
import java.util.*;



@Entity
public class Trainee extends User{



    @Column(nullable=true)
    private String address;
    @Column(nullable=true)
    private LocalDate dateOfBirth;





    @OneToMany(mappedBy = "trainee",cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
    })
    private Set<Training> trainings = new HashSet<>();
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
    })
    @JoinTable(
         name="trainee_trainer",
         joinColumns = @JoinColumn(name="trainee_id"),
         inverseJoinColumns = @JoinColumn(name="trainer_id")
    )
    private Set<Trainer> trainers = new HashSet<>();



    public Trainee(){
    }



    public void setAddress(String address) {
        this.address = address;
    }
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = LocalDate.parse(dateOfBirth);
    }
    public void setTrainer(Trainer trainer){
        trainers.add(trainer);
        trainer.setTrainee(this);
    }
    public void setTraining(Training training){
        this.trainings.add(training);
        training.setTrainee(this);
    }




    public LocalDate getBirthDate(){
        return this.dateOfBirth;
    }

    public Set<Trainer> getTrainers(){
        return this.trainers;
    }
    public Set<Training> getTrainings() {
        return this.trainings;
    }
    public String getAddress() {
        return this.address;
    }
    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public User getUser(){return this;}








    @Override
    public String toString(){
        return "{"+"UserID : "+this.getId() + ",name: " + this.getFirstName() + ",lastname: " +
                this.getLastName()+",username: "+this.getUserName()+",password: " + this.getPassword()+",activeStatus: "+
                this.isActive()+",dateOfBirth: "+this.dateOfBirth +",address: "+this.address+"}";
    }






}
