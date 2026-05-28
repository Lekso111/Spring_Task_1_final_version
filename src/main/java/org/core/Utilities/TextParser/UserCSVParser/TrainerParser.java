package org.core.Utilities.TextParser.UserCSVParser;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvException;
import org.core.models.Trainer;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TrainerParser implements UserParser<Trainer> {

    CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader("src/main/resources/trainersInitialData.csv"));
    Map<Integer,Trainer> trainerMap = new HashMap<>();
    Map<String,String> row;

    public TrainerParser() throws IOException {
    }


    @Override
    public Map<Integer, Trainer> parseCSV() throws CsvException, IOException {
        while((row = reader.readMap())!=null){
            trainerMap.put(Integer.parseInt(row.get("userId")),
                    new Trainer(row.get("firstName"),row.get("lastName"),row.get("userName"),
                            row.get("password"),Boolean.parseBoolean("active"),
                            row.get("specialization"),Integer.parseInt(row.get("userId"))));
        }
        reader.close();
        return trainerMap;
    }
}
