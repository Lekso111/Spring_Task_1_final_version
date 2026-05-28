package org.core.models;
import org.core.Utilities.PasswordGenerator;
import org.core.Utilities.UsernameGenerator;




public class Trainee extends User{



    private UsernameGenerator generator = new UsernameGenerator();
    private PasswordGenerator passwordGenerator = new PasswordGenerator();
    private  int serial = 0;
    private String address;
    private int dateOfBirth;
    private int  userId;




    public Trainee(String firstName,String lastName,String username,
                   String password,
                  boolean isActive,int birthDate,int userId,
                  String address) {
        super(firstName, lastName, username, password, isActive);
        this.setUsername(generator.generateUsername(this.getFirstName(),this.getLastName()));
        this.setPassword(passwordGenerator.generatePassword());
        this.dateOfBirth = birthDate;
        this.userId = userId;
        this.address = address;
    }


    @Override
    public String toString(){
        return "{"+"UserID : "+this.userId + ",name: " + this.firstName + ",lastname: " +this.lastName+",username: "+this.username+",password: " + this.password+",activeStatus: "+this.isActive+",dateOfBirth: "+this.dateOfBirth +",address: "+this.address+"}";

    }





    public void setDateOfBirth(int dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public int getDateOfBirth() {
        return dateOfBirth;
    }

    public int getUserId() {
        return userId;
    }




}
