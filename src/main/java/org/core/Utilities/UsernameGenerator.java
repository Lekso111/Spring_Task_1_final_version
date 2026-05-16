package org.core.Utilities;


import org.core.Storage.EmbeddedStorage;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;



public class UsernameGenerator {

    private static Map<String,Integer> generatedUsernames = new HashMap<>();
    EmbeddedStorage user_storage;


    @Autowired
    public void setUser_storage(EmbeddedStorage user_storage){
        this.user_storage = user_storage;
    }


     public String generateUsername(String firstname,String lastname){
         String username = firstname + "." + lastname;

         if(generatedUsernames.containsKey(username)){
             int updatedSerial = generatedUsernames.get(username);
             ++updatedSerial;
             generatedUsernames.put(username,updatedSerial);
         }else{
             generatedUsernames.put(username,0);
         }

         return username+ generatedUsernames.get(username);
     }

     public void displayUsernames(){
         for(Map.Entry<String,Integer> iterator : generatedUsernames.entrySet()){
             System.out.println(iterator);
         }
     }

}
