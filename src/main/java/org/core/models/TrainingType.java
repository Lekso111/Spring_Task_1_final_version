package org.core.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component("trainingType")
public class TrainingType {


    private final String name;

    public TrainingType(@Value("${training.type}")String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }


}
