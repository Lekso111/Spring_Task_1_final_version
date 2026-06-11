package org.core.entities;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;
import java.util.HashSet;
import java.util.Set;



@Entity
@Immutable
public class TrainingType{



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(nullable=false)
    private String name;

    @OneToMany(mappedBy="trainingType")
    private Set<Training> trainings = new HashSet<>();

    public TrainingType(){}


    public Long getId() {
        return this.id;
    }



    public void setName(String name) {
        this.name = name;
    }
    public void setTrainings(Training training){
        this.trainings.add(training);
        training.setTrainingType(this);
    }




    public String getName() {
        return name;
    }
    public Set<Training> getTrainings(){
        return this.trainings;
    }

}
