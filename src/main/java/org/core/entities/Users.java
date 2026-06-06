package org.core.entities;

import jakarta.persistence.*;
import org.core.Utilities.PasswordGenerator;
import org.core.Utilities.UsernameGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


@Component
@Entity
@Table(name="Users")
public class Users {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Integer id;
    @Column(nullable = false)
    public  String firstName;
    @Column(nullable=false)
    public String lastName;
    @Column(nullable = false)
    public String username;
    @Column(nullable = false)
    public  String password;
    @Column(nullable = false)
    boolean isActive;







    public Users(){};



    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserName(String username) {
        this.username = username;
    }


    //overloaded
    public void setUserName(){
        this.username = UsernameGenerator.generateUsername(this.getFirstName(),this.getLastName());
    }


    public void setPassword(String password) {
        this.password = password;
    }



    public void setPassword(){
        this.password = PasswordGenerator.generatePassword();
    }

    public void setActive(boolean active) {
        isActive = active;
    }




    public Integer getId(){return id;}
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getUserName() {
        return this.username;
    }
    public String getPassword() {
        return this.password;
    }

    public boolean isActive() {
        return isActive;
    }

    
}
