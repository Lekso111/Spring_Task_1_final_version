package org.core.models;

import org.core.Utilities.PasswordGeneration;
import org.core.Utilities.UsernameGeneration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component("trainee")
//@Scope("singleton")
public class Trainee extends User{



    public UsernameGeneration generator = new UsernameGeneration();
    public PasswordGeneration password_generator = new PasswordGeneration();
    public  int serial = 0;
    public String address;
    public int date_of_birth,userId;



    @Autowired
    public Trainee(@Value("${trainee.name}")String firstName, @Value("${trainee.lastname}")String lastName,@Value("${trainee.username}")String username,
                   @Value("${trainee.password}")String password,
                   @Value("${trainee.active}")boolean isActive, @Value("${trainee.birth}")int date_of_birth, @Value("${trainee.userId}")int userId,
                   @Value("${trainee.address}")String address) {
        super(firstName, lastName, username, password, isActive);
        this.username = generator.generateUsername(this.firstName,this.lastName);
        this.password = password_generator.generate_password();
        this.date_of_birth = date_of_birth;
        this.userId = userId;
        this.address = address;
    }



    public Trainee(){
        super();
    }


    @Override
    public String toString(){
        return "{"+"UserID : "+this.userId + ",name: " + this.firstName + ",lastname: " +this.lastName+",username: "+this.username+",password: " + this.password+",activeStatus: "+this.isActive+",date_of_birth: "+this.date_of_birth+",address: "+this.address+"}";

    }


 //setter injection
//    @Autowired
//    public void setUsernameGenerator(UsernameGeneration generator){
//        this.generator = generator;
//    }



//setter methods
    public void setDate_of_birth(int date_of_birth) {
        this.date_of_birth = date_of_birth;
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

    public void setAddress(String address) {
        this.address = address;
    }



}
