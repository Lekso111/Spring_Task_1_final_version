package org.core.Utilities;


import org.core.Storage.EmbeddedStorage;
import org.core.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service("usernameGenerator")
public class UsernameGenerator {

     String firstname,lastname;
     private User user;


    private static Map<String,Integer> generated_usernames = new HashMap<>();
     private  int serial = 0;




    EmbeddedStorage user_storage;


    @Autowired
    public void setUser_storage(EmbeddedStorage user_storage){
        this.user_storage = user_storage;
    }


     public String generateUsername(String firstname,String lastname){
         String username = firstname + "." + lastname;
//         if(generated_usernames.contains(username)){
//             serial++;
//             username+=serial;
//             generated_usernames.add(username);
//             return username;
//         }else {
//             generated_usernames.add(username);
//             return username;
//         }

         if(generated_usernames.containsKey(username)){
             int updated_serial = generated_usernames.get(username);
             ++updated_serial;
             generated_usernames.put(username,updated_serial);
         }else{
             generated_usernames.put(username,0);
         }

         return username+generated_usernames.get(username);
     }

     public void displayUsernames(){
         for(Map.Entry<String,Integer> iterator : generated_usernames.entrySet()){
             System.out.println(iterator);
         }
     }

     //maps gavaketeb sadac key ikneba firstname.lastname da value ikneba serial number
     //mag:{key = eke.pots,value = 0}
     // davushvat daemata axali eke.pots
     // geenerateUsername() naxavs tu sheicavs es map am axal eke.pots
    //roca naxavs rom gvakvs ukve eke.pots map.shi , mashin gaaketebs : 1.serial++ 2.eke.pots+=serial 3.daaupdatebs serials map shi

}
