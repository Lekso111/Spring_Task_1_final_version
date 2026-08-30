package org.core.Utilities;


//import org.core.Storage.EmbeddedStorage;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;




public class UsernameGenerator {

    private static Map<String,Integer> generatedUsernames = new HashMap<>();




     public static String generateUsername(String firstname,String lastname){
         String username = firstname + "." + lastname;

         if(generatedUsernames.containsKey(username)){
             int updatedSerial = generatedUsernames.get(username);
             ++updatedSerial;
             generatedUsernames.put(username,updatedSerial);
         }else{
             generatedUsernames.put(username,1);
         }

         return username + generatedUsernames.get(username);
     }

     public static void displayUsernames(){
         for(Map.Entry<String,Integer> iterator : generatedUsernames.entrySet()){
             System.out.println(iterator);
         }
     }

}
