package org.core.dao.UserDaoAbstraction;

import jakarta.annotation.PostConstruct;
import org.core.Storage.Embedded_Storage;
import org.core.models.Trainee;
import org.core.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.logging.Logger;


@Repository("traineeDao")
public class TraineeDao implements UserDao<Trainee>{

    public static Logger logger = Logger.getLogger("traineeDao logger");


    Embedded_Storage storage;

    @Autowired
    public void setStorage(Embedded_Storage storage){
        this.storage = storage;
    }


    Map<Integer,User> retrieved_storage;

    @PostConstruct
    public void init(){
        retrieved_storage = storage.get_storage();
    }

    @Override
    public void addUser(Trainee user) {
        if(!(retrieved_storage.get(user.userId)==user)){
            retrieved_storage.put(user.userId,user);
            logger.info("Trainee " +user.firstName+" with username : ''"+user.username+"'' was added");
        }
    }

    @Override
    public void updateUser(Trainee user,String updatedName) {

       try{
           if(!retrieved_storage.get(user.userId).equals(null)){
               Trainee updated_user = user;
               updated_user.firstName = updatedName;
               retrieved_storage.replace(user.userId,user,updated_user);
               logger.info("Trainee " +user.firstName+" with username : ''"+user.username+"'' was updated");
           }else{
               throw new NullPointerException();
           }
       }catch(NullPointerException e){
           logger.severe("Trainee " + user.firstName + " "+user.lastName+" with userid : " + user.userId + " was not found to be updated");
       }

    }

    @Override
    public Trainee selectUser(int id) {
        return (Trainee) retrieved_storage.get(id);
    }


    public void displayStorage(Map<Integer,User> storage_parametered){
        System.out.println(storage_parametered);
    }

    public void deleteTrainee(Trainee user) {
        try {
            if (!retrieved_storage.get(user.userId).equals(null)) {
                retrieved_storage.remove(user.userId);
                logger.severe("Trainee " +user.firstName+" with username : ''"+user.username+"'' was deleted");
            }else{
                throw new NullPointerException();
            }
        }catch (NullPointerException e){
            logger.severe("Trainee with userid " + user.userId+ " was not found in storage");
        }
//        logger.info("Trainee " +user.firstName+" with username : ''"+user.username+"'' was deleted");

    }



}
