package org.core.Utilities;

import java.util.Random;




public class PasswordGenerator {

   private static Random rnd = new Random();



    public static String generatePassword(){
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
