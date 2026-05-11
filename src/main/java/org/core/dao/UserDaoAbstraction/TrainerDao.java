package org.core.dao.UserDaoAbstraction;


import jakarta.annotation.PostConstruct;
import org.core.Storage.Embedded_Storage;
import org.core.models.Trainer;
import org.core.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.logging.Logger;

@Repository
public class TrainerDao implements UserDao<Trainer>{

    public static Logger logger = Logger.getLogger("Trainerdao logger");



    Embedded_Storage storage;

    @Autowired
    public void setStorage(Embedded_Storage storage){
        this.storage = storage;
    }

    Map<Integer, User> retrieved_storage;

    @PostConstruct
    public void init(){
        retrieved_storage = storage.get_storage();
    }

    @Override
    public void addUser(Trainer user) {

        if(!(retrieved_storage.get(user.userId)==(user))){
            retrieved_storage.put(user.userId,user);
            logger.info("Trainer " +user.firstName+" with username : ''"+user.username+"'' was added");
        }

    }

    @Override
    public void updateUser(Trainer user,String updatedName) {


        try{
            if(!retrieved_storage.get(user.userId).equals(null)){
                Trainer updated_user = user;
                String old_name = user.firstName;
                updated_user.firstName = updatedName;
                retrieved_storage.replace(user.userId,user,updated_user);
                logger.info("The name of  Trainer " + old_name + " with username: ''"+old_name+
                        "'' was renamed to ''" + user.username +"''");
            }else{
                throw new NullPointerException();
            }


        }catch(NullPointerException e){
            logger.severe("Trainer " + user.firstName + " " + user.lastName + " was not found to be updated");
        }



    }

    @Override
    public Trainer selectUser(int id) {
        return (Trainer) retrieved_storage.get(id);
    }
}
