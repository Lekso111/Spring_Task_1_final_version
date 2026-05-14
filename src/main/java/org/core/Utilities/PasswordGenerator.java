package org.core.Utilities;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service("passwordGenerator")
public class PasswordGenerator {

    Random rnd = new Random();





    private static List<String> generated_passwords = new ArrayList<>();


    public String generate_password(){
        String password = "";
        char[] letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        int random_integer;


        for(int i = 0;i<=10;i++){
            random_integer = rnd.nextInt(51);
            password+=letters[random_integer];
        }



        return password;
    }


}
