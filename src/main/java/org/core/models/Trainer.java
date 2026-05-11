package org.core.models;

import org.core.Utilities.PasswordGeneration;
import org.core.Utilities.UsernameGeneration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("trainer")
public class Trainer extends User{


   Training training;
   TrainingType trainingType;
   UsernameGeneration generator = new UsernameGeneration();
   PasswordGeneration password_generator = new PasswordGeneration();

    @Value("gym")
    public String specialisation;
    @Value("100")
    public int userId;


    @Autowired
    public Trainer(
            @Value("${trainer.firstname}") String firstname,
            @Value("${trainer.lastname}") String lastname,
            @Value("${trainer.username}") String username,
            @Value("${trainer.password}") String password,
            @Value("${trainer.active}") boolean isActive,
            @Value("${trainer.specialisation}") String specialisation,
            @Value("${trainer.userId}") int userId
    ) {
        super(firstname, lastname, username, password, isActive);
        this.username = generator.generateUsername(this.firstName,this.lastName);
        this.password = password_generator.generate_password();
        this.specialisation = specialisation;
        this.userId = userId;
    }


    public Trainer(){};



    @Override
    public String toString(){
        return "{"+"UserID : "+this.userId + ",name: " + this.firstName + ",lastname: " +this.lastName+",username: "+this.username+",password: " + this.password+",activeStatus: "+this.isActive+",specialisation: "+this.specialisation+"}";
    }


    //setter injection
    @Autowired
    public void setTraining(Training training){
        this.training = training;
    }

    @Autowired
    public void setTrainingType(TrainingType trainingType){
        this.trainingType = trainingType;
    }

    @Autowired
    public void setGenerator(UsernameGeneration generator){
        this.generator = generator;
    }

    @Autowired
    public void setPassword_generator(PasswordGeneration generator){
        this.password_generator = generator;
    }



    //setters for fields
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }


}
