package org.core.dto;

import org.core.Utilities.PasswordGenerator;
import org.core.Utilities.UsernameGenerator;

public class UserDTO {

    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private boolean isActive;




    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName() {
        this.userName = UsernameGenerator.generateUsername(this.getFirstName(),this.getLastName());
    }

    public String getPassword() {
        return password;
    }

    public void setPassword() {
        this.password = PasswordGenerator.generatePassword();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


}
