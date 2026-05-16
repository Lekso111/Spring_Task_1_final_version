package org.core.StorageTraining;


import com.opencsv.exceptions.CsvException;
import jakarta.annotation.PostConstruct;
import org.core.Utilities.TextParser.TrainingCSVParser.TrainingParser;
import org.core.models.Training;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Repository
public class TrainingStorage {


    private TrainingParser parser = new TrainingParser();
    private Map<String, Training> trainingStorage = new HashMap<>();

    public TrainingStorage() throws IOException {
    }


    @PostConstruct
    public void init() throws IOException, CsvException {
        trainingStorage.putAll(parser.parseCSV());
    }


    public void add(Training training){
        trainingStorage.put(training.getName(),training);
    }

    public Training select(Training training){
        return trainingStorage.get(training.getName());
    }

    public Map<String,Training> getTrainingStorage(){
        return trainingStorage;
    }


}
