package org.gym.component;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.core.entities.Trainer;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class TrainingSteps {

    private final ComponentTestContext context;

    public TrainingSteps(ComponentTestContext context) {
        this.context = context;
    }

    @Given("a trainer {string} named {string} {string} is registered")
    public void aTrainerIsRegistered(String username, String firstName, String lastName) {
        Trainer trainer = context.buildTrainer(username, firstName, lastName);
        when(context.trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));
    }

    @When("a training on {string} lasting {double} hours is added for trainee {string} with trainer {string}")
    public void aTrainingIsAdded(String date, double duration, String traineeUsername, String trainerUsername)
            throws Exception {
        String body = "{\"traineeUsername\":\"" + traineeUsername + "\",\"trainerUsername\":\"" + trainerUsername + "\","
                + "\"trainingName\":\"Morning session\",\"trainingDate\":\"" + date + "\",\"trainingDuration\":"
                + duration + "}";
        context.setLastResult(context.mockMvc.perform(post("/api/trainings")
                .contentType(APPLICATION_JSON)
                .content(body)));
    }

    @When("a training with no duration is added for trainee {string} with trainer {string}")
    public void aTrainingWithNoDurationIsAdded(String traineeUsername, String trainerUsername) throws Exception {
        String body = "{\"traineeUsername\":\"" + traineeUsername + "\",\"trainerUsername\":\"" + trainerUsername + "\","
                + "\"trainingName\":\"Morning session\",\"trainingDate\":\"2024-05-10\"}";
        context.setLastResult(context.mockMvc.perform(post("/api/trainings")
                .contentType(APPLICATION_JSON)
                .content(body)));
    }

    @Then("a workload event is published")
    public void aWorkloadEventIsPublished() {
        verify(context.jmsTemplate, times(1)).convertAndSend(any(String.class), any(), any());
    }

    @Then("no workload event is published")
    public void noWorkloadEventIsPublished() {
        verify(context.jmsTemplate, never()).convertAndSend(any(String.class), any(), any());
    }
}
