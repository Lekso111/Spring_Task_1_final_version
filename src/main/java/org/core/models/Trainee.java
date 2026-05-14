package org.core.models;

import org.core.Utilities.PasswordGenerator;
import org.core.Utilities.UsernameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component("trainee")
public class Trainee extends User{



    private UsernameGenerator generator = new UsernameGenerator();
    private PasswordGenerator passwordGenerator = new PasswordGenerator();
    private  int serial = 0;
    private String address;
    private int date_of_birth,userId;

    @Autowired
    public Trainee(@Value("${trainee.name}")String firstName, @Value("${trainee.lastname}")String lastName,@Value("${trainee.username}")String username,
                   @Value("${trainee.password}")String password,
                   @Value("${trainee.active}")boolean isActive, @Value("${trainee.birth}")int date_of_birth, @Value("${trainee.userId}")int userId,
                   @Value("${trainee.address}")String address) {
        super(firstName, lastName, username, password, isActive);
        this.setUsername(generator.generateUsername(this.getFirstName(),this.getLastName()));
        this.setPassword(passwordGenerator.generate_password());
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


    public UsernameGenerator getGenerator() {
        return generator;
    }

    public PasswordGenerator getPassword_generator() {
        return passwordGenerator;
    }

    public int getSerial() {
        return serial;
    }

    public String getAddress() {
        return address;
    }

    public int getDate_of_birth() {
        return date_of_birth;
    }

    public int getUserId() {
        return userId;
    }




}
