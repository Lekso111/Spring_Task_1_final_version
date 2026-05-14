package org.core.Utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TextFileParser {
    private String fileName;
    private Map<String, String> parsedContent = new HashMap<>();
    private BufferedReader reader;

    public TextFileParser(String name) {
        this.fileName = name;
    }




    public Map<String, String> getParsedFile() {

        try {
            reader = new BufferedReader(new FileReader(fileName));
            String currentLine = reader.readLine();

            while(currentLine!=null){
                String key = currentLine.split("=")[0];
                String value = currentLine.split("=")[1];
                parsedContent.put(key,value);
                currentLine = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return parsedContent;
    }




}
