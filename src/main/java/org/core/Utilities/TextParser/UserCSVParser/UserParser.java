package org.core.Utilities.TextParser.UserCSVParser;

import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.util.Map;

public interface UserParser<T> {

    public Map<Integer,T> parseCSV() throws CsvException, IOException;
}
