package org.core.entities;

import jakarta.persistence.*;
import org.core.Utilities.PasswordGenerator;
import org.core.Utilities.UsernameGenerator;
import org.core.dto.UserDTO;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Scope("prototype")
@Entity
public class Trainer implements Serializable {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToMany(mappedBy="trainer")
    private Set<Training> specialization = new HashSet<>();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="userId")
    public Users userId = new Users();
    @ManyToMany(mappedBy="trainers")
   private Set<Trainee> trainees = new HashSet<>();


    //no-args constructor
    public Trainer(){
    }



    public void setSpecialization(Training specialization) {
        this.specialization.add(specialization);
        specialization.setTrainer(this);
    }


    public void setTrainee(Trainee trainee){
        this.trainees.add(trainee);
    }







    //no need to overload
    public Users getUser(){
        return this.userId;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.userId.setFirstName(this.getFirstName());
    }


    public boolean isActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
        this.userId.setActive(this.isActiveStatus());
    }



    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.userId.setLastName(this.getLastName());
    }

    public String getPassword() {
        return password;
    }

    public void setPassword() {
        this.password = PasswordGenerator.generatePassword();
        this.userId.setPassword(this.getPassword());
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName() {
        this.userName = UsernameGenerator.generateUsername(this.getFirstName(),this.getLastName());
        this.userId.setUserName(this.getUserName());
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







    @Transient
    private String firstName;
    @Transient
    private String lastName;
    @Transient
    private String userName;
    @Transient
    private String password;
    @Transient
    private boolean activeStatus;




    @Override
    public String toString(){
        return "{"+"UserID : "+this.getId() + ",name: " + this.userId.firstName + ",lastname: " +
                this.userId.lastName+",username: "+this.userId.username+",password: "
                + this.userId.password+
                ",activeStatus: "+this.userId.isActive+",specialization: "+this.specialization +"}";
    }



}
