package org.core.models;

import org.core.Utilities.PasswordGenerator;
import org.core.Utilities.UsernameGenerator;
import org.springframework.beans.factory.annotation.Autowired;

public class Trainer extends User{

   private Training training;
   private TrainingType trainingType;
   private UsernameGenerator generator = new UsernameGenerator();
   private PasswordGenerator passwordGenerator = new PasswordGenerator();
   private String specialization;
   private int userId;


    public Trainer(
           String firstName,String lastName,String userName,String password,
           boolean isActive,String specialization,int userId
    ) {
        super(firstName, lastName, userName, password, isActive);
        this.username = generator.generateUsername(this.firstName,this.lastName);
        this.password = passwordGenerator.generatePassword();
        this.specialization = specialization;
        this.userId = userId;
    }



    @Autowired
    public void setTraining(Training training){
        this.training = training;
    }

    @Autowired
    public void setTrainingType(TrainingType trainingType){
        this.trainingType = trainingType;
    }

    @Autowired
    public void setGenerator(UsernameGenerator generator){
        this.generator = generator;
    }

    @Autowired
    public void setPasswordGenerator(PasswordGenerator generator){
        this.passwordGenerator = generator;
    }


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
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }



    public Training getTraining() {
        return training;
    }
    public TrainingType getTrainingType() {
        return trainingType;
    }
    public UsernameGenerator getGenerator() {
        return generator;
    }
    public PasswordGenerator getPasswordGenerator() {
        return passwordGenerator;
    }
    public String getSpecialization() {
        return specialization;
    }
    public int getUserId() {
        return userId;
    }

    @Override
    public String toString(){
        return "{"+"UserID : "+this.userId + ",name: " + this.firstName + ",lastname: " +this.lastName+",username: "+this.username+",password: " + this.password+",activeStatus: "+this.isActive+",specialization: "+this.specialization +"}";
    }



}
