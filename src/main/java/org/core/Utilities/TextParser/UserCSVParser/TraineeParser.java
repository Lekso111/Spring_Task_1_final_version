package org.core.Utilities.TextParser.UserCSVParser;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import org.core.models.Trainee;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TraineeParser implements UserParser<Trainee> {

    private CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader("src/main/resources/traineesInitialData.csv"));
    private Map<Integer,Trainee> traineeMap = new HashMap<>();
    private Map<String,String> row;

    public TraineeParser() throws IOException {
    }


    @Override
    public Map<Integer,Trainee> parseCSV() throws CsvValidationException, IOException {
        while((row = reader.readMap())!=null){
            traineeMap.put(
                    Integer.parseInt(row.get("userId")),new Trainee(row.get("firstName"),row.get("lastName"),row.get("userName"),
                            row.get("password"),Boolean.parseBoolean(row.get("active")),
                            Integer.parseInt(row.get("birthYear")),Integer.parseInt(row.get("userId")),row.get("address")
            ));
        }
        reader.close();
        return traineeMap;
    }


}