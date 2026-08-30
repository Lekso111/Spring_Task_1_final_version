package org.core.repositories.UserRepository;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import org.core.entities.*;
import org.core.repositories.TrainingRepository.TrainingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;



@Repository
public class TraineeRepo implements UserRepo<Trainee>{

    Logger logger = Logger.getLogger("Trainee repository logger");
    EntityManager entityManager =
           Persistence.createEntityManagerFactory("myPersistenceUnit").createEntityManager();

    TrainerRepo trainerRepo;
    TrainingRepo trainingRepo;

    @Autowired
    public TraineeRepo(TrainerRepo trainerRepo,TrainingRepo trainingRepo){
        this.trainerRepo = trainerRepo;
        this.trainingRepo = trainingRepo;
    }




    @Override
    public void add(Trainee trainee){
        try{
                entityManager.getTransaction().begin();
                if (trainee.getId() == null) {
                    entityManager.persist(trainee);
                } else {
                    logger.severe("You already have following Trainee instance persisted");
                }
                entityManager.getTransaction().commit();
        }catch (Exception e){
            System.out.println("Selected entity was not found");
            e.printStackTrace();
        }
    }






    @Override
    public void update(Trainee updatableTrainee,String username,String password,boolean activeStatus) throws Exception {
        if(this.verify(updatableTrainee,username,password)) {
            try {
                Trainee trainee = entityManager.find(Trainee.class, updatableTrainee.getId());
                entityManager.getTransaction().begin();
                if (entityManager.contains(trainee)) {
                    User user = trainee.getUser();
                    user.setActive(activeStatus);
                    User updatedUser = entityManager.merge(user);
                }
                entityManager.getTransaction().commit();
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                System.out.println(updatableTrainee.getId());
                logger.severe("You have still not persisted the following Trainee instance.The reason JVM threw an IllegalArgumentException is that your Trainee instance was not persisted before updating" +
                        ",so Id was not auto-generated for this entity instance.Persist the following Trainee instace first and only after update that ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            logger.severe("Credentials are incorrect");
            throw new IllegalAccessException();
        }
    }





    @Override
    public Optional<Trainee> select(String username,String password) throws Exception {

        User retrievedUser = this.selectUser(username,password);
        TypedQuery<Trainee> query =
                entityManager.createQuery("SELECT t FROM Trainee t WHERE t.id = :userId ",
                        Trainee.class);
        query.setParameter("userId", retrievedUser.getId());
        Trainee trainee = query.getSingleResult();
        Trainee retrievedTrainee = entityManager.find(Trainee.class,trainee.getId());

        return (retrievedTrainee.getId()!=null)?Optional.of(retrievedTrainee):Optional.empty();
    }





    @Override
    public boolean verify(Trainee trainee, String username, String password) throws Exception{
        Trainee retrievedTrainee = this.select(username,password).get();
        User referencedUser = retrievedTrainee.getUser();
        if(referencedUser.getUserName().equals(username) && referencedUser.getPassword().equals(password)){
            return true;
        }
        return false;
    }




    public void deleteByUsername(String username,String password){
        try{
            Trainee trainee = this.select(username,password).get();
            Trainee retrievedTrainee = entityManager.find(Trainee.class,trainee.getId());
            if(this.verify(trainee,username,password)) {
                entityManager.getTransaction().begin();
                if (entityManager.contains(retrievedTrainee)) {
                    Set<Training> retrievedTrainings = retrievedTrainee.getTrainings();

                    for(Training training : retrievedTrainings){
                        Training referencedTraining = entityManager.find(Training.class,training.getId());
                        referencedTraining.setTrainee(null);
                        Training mergedTraining = entityManager.merge(referencedTraining);
                    }

                    for(Trainer trainer : retrievedTrainee.getTrainers()){
                        trainer.getTrainees().remove(retrievedTrainee);
                    }

                    retrievedTrainee.getTrainers().clear();
                    entityManager.remove(retrievedTrainee);
                    logger.severe("Following Trainee has been succesfully deleted");
                }
                entityManager.getTransaction().commit();
            }else{
                logger.severe("Following Trainee was not found to be updated");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }





    @Override
    public User selectUser(String username, String password) throws Exception{
        try{

            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.username=:username AND u.password=:password", User.class
            );

            query.setParameter("username",username);
            query.setParameter("password",password);
            User retrievedUser = query.getSingleResult();
            return retrievedUser;
        }catch(Exception e){
            e.printStackTrace();
        }
        throw new EntityNotFoundException("Your desired User instance has not been found");
    }




    public void updatePassword(String username,String oldPassword,String newPassword){
        try{
            Trainee updatedPasswordTrainee = this.select(username,oldPassword).get();
            Trainee latestTrainee = entityManager.find(Trainee.class,updatedPasswordTrainee.getId());
            if(this.verify(latestTrainee,username,oldPassword)){
                entityManager.getTransaction().begin();
                User mappedUser = latestTrainee.getUser();
                mappedUser.setPassword(newPassword);
                System.out.println("Current : " + mappedUser.getPassword());
                User trainee = entityManager.merge(mappedUser);
                entityManager.getTransaction().commit();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }




    @Override
    public Trainee selectByUsername(String username) throws Exception{
        TypedQuery<User> userQuery = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username=:username", User.class
        );
        userQuery.setParameter("username",username);
        User referencedUser = userQuery.getSingleResult();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Trainee> query = criteriaBuilder.createQuery(Trainee.class);
        Root<Trainee> root = query.from(Trainee.class);

        Predicate userIdPredicate = criteriaBuilder.equal(root.get("id"),referencedUser.getId());
        CriteriaQuery<Trainee> queryTrainee = query.select(root).where(criteriaBuilder.and(userIdPredicate));
        Query query1 = entityManager.createQuery(queryTrainee);
        Trainee trainee =(Trainee) query1.getSingleResult();
        return trainee;
    }



    @Override
    public void assignTraining(String username,Training training) throws Exception {


        Training retrievedTraining = entityManager.find(Training.class, training.getId());
        Trainee retrievedTrainee = this.selectByUsername(username);
        Trainee assignedTrainee = entityManager.find(Trainee.class, retrievedTrainee.getId());

        try {
            entityManager.getTransaction().begin();
            assignedTrainee.setTraining(retrievedTraining);
            Trainee mergedTrainee = entityManager.merge(assignedTrainee);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    @Override
    public List<Training> getTrainings(String userName, String fromDate,
                                       String toDate, String trainerUserName,
                                       TrainingType trainingType) throws Exception {
        Trainee referencedTrainee = this.selectByUsername(userName);
        Trainee retrievedTrainee = entityManager.find(Trainee.class,referencedTrainee.getId());
        LocalDate rangeFrom = LocalDate.parse(fromDate);
        LocalDate rangeTo = LocalDate.parse(toDate);

        Trainer referencedTrainer = trainerRepo.selectByUsername(trainerUserName);
        Trainer retrievedTrainer = entityManager.find(Trainer.class,referencedTrainer.getId());


        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> queryObject = criteriaBuilder.createQuery(Training.class);
        Root<Training> root = queryObject.from(Training.class);
        Predicate traineeIdPredicate = criteriaBuilder.equal(root.get("trainee").get("id"), retrievedTrainee.getId());
        Predicate trainingTypePredicate = criteriaBuilder.equal(root.get("trainingType").get("id"), trainingType.getId());
        Predicate datePredicate = criteriaBuilder.between(
                root.get("date"), rangeFrom, rangeTo
        );
        Predicate trainerIdPredicate = criteriaBuilder.equal(root.get("trainer").get("id"), retrievedTrainer.getId());
        queryObject.select(root).where(criteriaBuilder.and(
                traineeIdPredicate,
                trainingTypePredicate,
                datePredicate,
                trainerIdPredicate
        ));


        List<Training> retrievedTrainings = entityManager.createQuery(queryObject).getResultList();
        return retrievedTrainings;
    }


    public void assignTrainer(Trainee trainee,Trainer trainer){
        if(trainer.getId()!=null){
            Trainee retrievedTrainee = entityManager.find(Trainee.class,trainee.getId());
            Trainer retrievedTrainer = entityManager.find(Trainer.class,trainer.getId());
            retrievedTrainee.setTrainer(retrievedTrainer);
            Trainee mergedTrainee = entityManager.merge(retrievedTrainee);
            Trainer mergedTrainer = entityManager.merge(retrievedTrainer);
        }else{
            logger.severe("Following Trainer instance is detached or has never been persisted.");
        }
    }


}
