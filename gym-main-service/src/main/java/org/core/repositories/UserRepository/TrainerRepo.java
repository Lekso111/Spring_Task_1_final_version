package org.core.repositories.UserRepository;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.core.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


@Repository
public class TrainerRepo implements UserRepo<Trainer>{

    Logger logger = Logger.getLogger("Trainee repostiory logger");

    @PersistenceContext
    EntityManager entityManager =
            Persistence.createEntityManagerFactory("myPersistenceUnit").createEntityManager();



    TraineeRepo traineeRepo;

    @Autowired
    public TrainerRepo(@Lazy TraineeRepo traineeRepo){
        this.traineeRepo = traineeRepo;
    }



    @Override
    public void add(Trainer user) {
        try{
            entityManager.getTransaction().begin();
            if(user.getId() == null){
                entityManager.persist(user);
            }else{
                logger.severe("You already have following Trainer instance persisted");
            }
            entityManager.getTransaction().commit();
        }catch(Exception e){
            e.printStackTrace();
        }
    }




    @Override
    public void update(Trainer trainer,String username,String password, boolean activeStatus) throws Exception {
        if(this.verify(trainer,username,password)) {
            try {
                Trainer retrievedTrainer = entityManager.getReference(Trainer.class, trainer.getId());
                entityManager.getTransaction().begin();
                if (entityManager.contains(retrievedTrainer)) {
                    User user = retrievedTrainer.getUser();
                    user.setActive(activeStatus);
                    User updatedUser = entityManager.merge(user);
                } else {
                    logger.severe("Persistence context hasn't following Trainer instance persisted");
                }
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            logger.severe("Credentials are incorrect");
            throw new IllegalAccessException();
        }
    }





    @Override
    public Optional<Trainer> select(String username,String password) throws Exception {
        User retrievedUser = this.selectUser(username,password);
        TypedQuery<Trainer> query = entityManager.createQuery(
                "SELECT t FROM Trainer t WHERE t.id=:userId",Trainer.class
        );
        query.setParameter("userId",retrievedUser.getId());
        Trainer selectedTrainer = query.getSingleResult();
        Trainer retrievedTrainer = entityManager.find(Trainer.class,selectedTrainer.getId());
        return (retrievedTrainer.getId()!=null)?Optional.of(selectedTrainer):Optional.empty();
    }

    @Override
    public boolean verify(Trainer trainer, String username, String password) throws Exception{
        Trainer retrievedTrainer = this.select(username,password).get();
        User retrievedUser = retrievedTrainer.getUser();
        if(retrievedUser.getUserName().equals(username) && retrievedUser.getPassword().equals(password)){
            return true;
        }
        return false;
    }

    @Override
    public User selectUser(String username, String password) throws Exception {
        try{
            TypedQuery<User> usersTypedQuery = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.username=:username AND u.password=:password", User.class

            );
            usersTypedQuery.setParameter("username",username);
            usersTypedQuery.setParameter("password",password);
            User retrievedUser = usersTypedQuery.getSingleResult();
            return retrievedUser;
        }catch (Exception e){
            e.printStackTrace();
        }
        throw new EntityNotFoundException("Your User instance has not been found");
    }




    public void updatePassword(String username,String oldPassword,String newPassword){
        try{
            Trainer updatedPasswordTrainer = this.select(username,oldPassword).get();
            Trainer latestTrainer = entityManager.find(Trainer.class,updatedPasswordTrainer.getId());
                if (this.verify(latestTrainer, username, oldPassword)) {
                    entityManager.getTransaction().begin();
                    User mappedUser = latestTrainer.getUser();
                    mappedUser.setPassword(newPassword);
                    System.out.println("Trainer current password : " + mappedUser.getPassword());
                    User updatedTrainer = entityManager.merge(mappedUser);
                    entityManager.getTransaction().commit();
                }
        }catch(Exception e){
            e.printStackTrace();
        }
    }




    @Override
    public List<Training> getTrainings(String userName, String fromDate, String toDate, String traineeUsername, TrainingType trainingType) throws Exception {
        Trainer retrievedTrainer = this.selectByUsername(userName);
        Trainee retrievedTrainee = traineeRepo.selectByUsername(traineeUsername);
        LocalDate parsedFrom = LocalDate.parse(fromDate);
        LocalDate parsedTo = LocalDate.parse(toDate);


        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> queryObject = criteriaBuilder.createQuery(Training.class);
        Root<Training> root = queryObject.from(Training.class);
        Predicate trainerIdPredicate = criteriaBuilder.equal(root.get("trainer").get("id"),retrievedTrainer.getId());
        Predicate trainingTypePredicate = criteriaBuilder.equal(root.get("trainingType").get("id"),trainingType.getId());
        Predicate datePredicate = criteriaBuilder.between(
                root.get("date"),parsedFrom,parsedTo
        );
        Predicate traineeIdPredicate = criteriaBuilder.equal(root.get("trainee").get("id"),retrievedTrainee.getId());


        queryObject.select(root).where(criteriaBuilder.and(
                trainerIdPredicate,
                trainingTypePredicate,
                datePredicate,
                traineeIdPredicate
        ));


        List<Training> retrievedTrainings = entityManager.createQuery(queryObject).getResultList();
        return retrievedTrainings;
    }

    @Override
    public Trainer selectByUsername(String username) throws Exception {
        TypedQuery<User> userQuery = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username=:username", User.class

        );
        userQuery.setParameter("username",username);
        User referencedUser = (User) userQuery.getSingleResult();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Trainer> query = criteriaBuilder.createQuery(Trainer.class);
        Root<Trainer> root = query.from(Trainer.class);
        Predicate userIdPredicate = criteriaBuilder.equal(root.get("id"),referencedUser.getId());
        CriteriaQuery<Trainer> queryTrainer = query.select(root).where(criteriaBuilder.and(userIdPredicate));
        Query query1 = entityManager.createQuery(queryTrainer);
        Trainer trainer =(Trainer) query1.getSingleResult();
        Trainer referencedTrainer = entityManager.find(Trainer.class,trainer.getId());

        return referencedTrainer;

    }

    @Override
    public void assignTraining(String username, Training training) throws Exception {
        Training retrievedTraining = entityManager.find(Training.class, training.getId());
        Trainer retrievedTrainer = this.selectByUsername(username);
        Trainer assignedTrainer = entityManager.find(Trainer.class, retrievedTrainer.getId());
        try {
            entityManager.getTransaction().begin();
            assignedTrainer.setSpecialization(retrievedTraining);
            Trainer mergedTrainer = entityManager.merge(assignedTrainer);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void assignTrainee(Trainer trainer,Trainee trainee){
        if(trainee.getId()!=null){
            entityManager.getTransaction().begin();
            Trainee retrievedTrainee = entityManager.find(Trainee.class,trainee.getId());
            Trainer retrievedTrainer = entityManager.find(Trainer.class,trainer.getId());
            retrievedTrainer.setTrainee(retrievedTrainee);
            retrievedTrainee.setTrainer(retrievedTrainer);
            Trainee mergedTrainee = entityManager.merge(retrievedTrainee);
            Trainer mergedTrainer = entityManager.merge(retrievedTrainer);
            entityManager.getTransaction().commit();
        }else{
            logger.severe("Following Trainer instance is detached or has never been persisted.");
        }
    }


}
