package org.core.entities;

import jakarta.persistence.*;
import org.core.Utilities.PasswordGenerator;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Trainer extends User{



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @OneToMany(mappedBy="trainer")
    private Set<Training> specialization = new HashSet<>();



    @ManyToMany(mappedBy="trainers")
   private Set<Trainee> trainees = new HashSet<>();





    public Trainer(){

    }
    public void setSpecialization(Training specialization) {
        this.specialization.add(specialization);
        specialization.setTrainer(this);
    }
    public void setTrainee(Trainee trainee){
        this.trainees.add(trainee);
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword() {
        this.password = PasswordGenerator.generatePassword();
    }
    public Integer getId(){
          return this.id;
        }
    public Set<Trainee> getTrainees() {
        return this.trainees;
    }
    public void setTrainees(Set<Trainee> trainees) {
        this.trainees = trainees;
    }
    public User getUser(){
            return this;
    }





    @Override
    public String toString(){
        return "{"+"UserID : "+this.getId() + ",name: " + this.firstName + ",lastname: " +
                this.lastName+",username: "+this.username+",password: "
                + this.password+
                ",activeStatus: "+this.isActive+",specialization: "+this.specialization +"}";
    }



}
