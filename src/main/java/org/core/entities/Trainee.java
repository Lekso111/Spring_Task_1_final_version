package org.core.entities;
import jakarta.persistence.*;
import org.core.Utilities.PasswordGenerator;
import org.core.Utilities.UsernameGenerator;
import org.core.dto.UserDTO;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;


@Component
@Scope("prototype")
@Entity
public class Trainee implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer  id;
    @Column(nullable=true)
    private String address;
    @Column(nullable=true)
    private LocalDate dateOfBirth;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="userId")
    private Users userId = new Users();
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





    @Transient
    private String firstName;
    @Transient
    private String lastName;
    @Transient
    private String userName;
    @Transient
    private String password;
    @Transient
    private boolean isActive;
    @Transient
    private UsernameGenerator generator = new UsernameGenerator();
    @Transient
    private PasswordGenerator passwordGenerator = new PasswordGenerator();
    @Transient
    private  int serial = 0;



    //no-args constructor
    public Trainee(){}


    public void setFirstName(String firstName){
        this.firstName = firstName;
        this.userId.setFirstName(this.getFirstName());
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
        this.userId.setLastName(this.getLastName());
    }
    public void setUserName(){
        this.userName = UsernameGenerator.generateUsername(this.getFirstName(),this.getLastName());
        this.userId.setUserName(this.getUserName());
    }
    public void setPassword(){
        this.password = PasswordGenerator.generatePassword();
        this.userId.setPassword(this.getPassword());
    }
    public void setActive(boolean active){
        this.isActive = active;
        this.userId.setActive(this.getActiveStatus());
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



    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public String getUserName(){
        return this.userName;
    }
    public String getPassword(){
        return this.password;
    }
    public boolean getActiveStatus(){
        return this.isActive;
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
    public Integer getId() {
        return this.id;
    }
    public Users getUser(){return this.userId;}








    @Override
    public String toString(){
        return "{"+"UserID : "+this.id + ",name: " + this.userId.getFirstName() + ",lastname: " +
                this.userId.getLastName()+",username: "+this.userId.getUserName()+",password: " + this.userId.getPassword()+",activeStatus: "+
                this.userId.isActive()+",dateOfBirth: "+this.dateOfBirth +",address: "+this.address+"}";
    }






}
