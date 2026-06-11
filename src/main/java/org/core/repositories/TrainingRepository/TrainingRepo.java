package org.core.repositories.TrainingRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.core.entities.Training;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.logging.Logger;


@Repository
public class TrainingRepo implements TrainingRepositoryInterface<Training> {


   EntityManager entityManager = Persistence.createEntityManagerFactory("myPersistenceUnit").createEntityManager();
   Logger logger = Logger.getLogger("Training logger");




    @Override
    public void add(Training training) {

        try{
            entityManager.getTransaction().begin();


            if(!entityManager.contains(training)){
                entityManager.persist(training);
                logger.severe("Following Training has been inserted successfully");
            }else{
                logger.severe("Following Training already exists ");
            }

            entityManager.persist(training);
            entityManager.getTransaction().commit();
        }catch(Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public Optional<Training> selectById(Integer id) {
        Training training = entityManager.find(Training.class,id);
        return (training.getId()!=null)?Optional.of(training):Optional.empty();
    }


}
