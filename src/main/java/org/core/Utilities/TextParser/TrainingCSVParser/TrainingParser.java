package org.core.Utilities.TextParser.TrainingCSVParser;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvException;
import org.core.models.Training;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TrainingParser {

    private CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader("src/main/resources/trainingsInitialData"));
    private Map<String,String> rows;
    private Map<String, Training> trainingMap = new HashMap<>();

    public TrainingParser() throws IOException{}




    public Map<String,Training> parseCSV() throws CsvException,IOException {
        while((rows = reader.readMap())!=null){
            trainingMap.put(rows.get("trainingName"),
                    new Training(
                            rows.get("trainingName"),
                            rows.get("trainingDate"),
                            Double.parseDouble(rows.get("trainingDuration")),
                            Integer.parseInt(rows.get("trainingTraineeId")),
                            Integer.parseInt(rows.get("trainingTrainerId")),
                            rows.get("trainingType")
                    ));
        }

        return trainingMap;
    }



}
