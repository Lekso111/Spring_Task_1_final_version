package org.gym.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.core.entities.Trainee;
import org.core.entities.Trainer;
import org.core.entities.TrainingType;
import org.gym.controller.LoginController;
import org.gym.controller.TraineeController;
import org.gym.controller.TrainerController;
import org.gym.controller.TrainingController;
import org.gym.controller.TrainingTypeController;
import org.gym.exception.GlobalExceptionHandler;
import org.gym.mapper.GymMapper;
import org.gym.repository.TraineeRepository;
import org.gym.repository.TrainerRepository;
import org.gym.repository.TrainingRepository;
import org.gym.repository.TrainingTypeRepository;
import org.gym.repository.UsersRepository;
import org.gym.security.JwtService;
import org.gym.security.LoginAttemptService;
import org.gym.service.CredentialGenerator;
import org.gym.service.LoginService;
import org.gym.service.TraineeService;
import org.gym.service.TrainerService;
import org.gym.service.TrainingService;
import org.gym.service.TrainingTypeService;
import org.gym.workload.WorkloadMessageProducer;
import org.gym.workload.WorkloadNotificationService;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;

public class ComponentTestContext {

    private static final String JWT_SECRET = "component-test-secret-key-that-is-long-enough-to-sign";

    final TraineeRepository traineeRepository = mock(TraineeRepository.class);
    final TrainerRepository trainerRepository = mock(TrainerRepository.class);
    final TrainingRepository trainingRepository = mock(TrainingRepository.class);
    final TrainingTypeRepository trainingTypeRepository = mock(TrainingTypeRepository.class);
    final UsersRepository usersRepository = mock(UsersRepository.class);
    final JmsTemplate jmsTemplate = mock(JmsTemplate.class);
    final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);

    final MockMvc mockMvc;

    private ResultActions lastResult;

    public ComponentTestContext() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        JwtService jwtService = new JwtService(JWT_SECRET, 3_600_000L);
        LoginAttemptService loginAttemptService = new LoginAttemptService();
        GymMapper mapper = new GymMapper();
        CredentialGenerator credentialGenerator = new CredentialGenerator(usersRepository);

        WorkloadMessageProducer workloadMessageProducer = new WorkloadMessageProducer(jmsTemplate);
        WorkloadNotificationService workloadNotificationService = new WorkloadNotificationService(workloadMessageProducer);

        TraineeService traineeService = new TraineeService(traineeRepository, trainerRepository, trainingRepository,
                credentialGenerator, mapper, passwordEncoder, jwtService, workloadNotificationService);
        TrainerService trainerService = new TrainerService(trainerRepository, trainingRepository, trainingTypeRepository,
                credentialGenerator, mapper, passwordEncoder, jwtService);
        TrainingService trainingService = new TrainingService(trainingRepository, traineeRepository, trainerRepository,
                workloadNotificationService);
        LoginService loginService = new LoginService(usersRepository, authenticationManager, jwtService,
                loginAttemptService, passwordEncoder);
        TrainingTypeService trainingTypeService = new TrainingTypeService(trainingTypeRepository, mapper);

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc = MockMvcBuilders.standaloneSetup(
                        new TraineeController(traineeService),
                        new TrainerController(trainerService),
                        new TrainingController(trainingService),
                        new LoginController(loginService),
                        new TrainingTypeController(trainingTypeService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    ResultActions getLastResult() {
        return lastResult;
    }

    void setLastResult(ResultActions lastResult) {
        this.lastResult = lastResult;
    }

    Trainee buildTrainee(String username, String firstName, String lastName) {
        Trainee trainee = new Trainee();
        trainee.setFirstName(firstName);
        trainee.setLastName(lastName);
        trainee.getUser().setUserName(username);
        trainee.getUser().setPassword("encoded-password");
        trainee.setActive(true);
        return trainee;
    }

    Trainer buildTrainer(String username, String firstName, String lastName) {
        TrainingType trainingType = new TrainingType();
        trainingType.setName("CARDIO-BASED");

        Trainer trainer = new Trainer();
        trainer.setFirstName(firstName);
        trainer.setLastName(lastName);
        trainer.getUser().setUserName(username);
        trainer.getUser().setPassword("encoded-password");
        trainer.setActiveStatus(true);
        trainer.setTrainingType(trainingType);
        return trainer;
    }
}
