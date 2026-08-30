package org.gym.workload.cucumber;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.gym.workload.dto.ActionType;
import org.gym.workload.dto.TrainerWorkloadRequest;
import org.gym.workload.messaging.WorkloadMessaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WorkloadIntegrationSteps {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScenarioState state;

    @When("the main service publishes an ADD workload event for trainer {string} of {double} hours on {string}")
    public void publishAddEvent(String username, double hours, String date) {
        publish(new TrainerWorkloadRequest(username, "First", "Last", true,
                LocalDate.parse(date), hours, ActionType.ADD));
    }

    @When("the main service publishes a DELETE workload event for trainer {string} of {double} hours on {string}")
    public void publishDeleteEvent(String username, double hours, String date) {
        publish(new TrainerWorkloadRequest(username, "First", "Last", true,
                LocalDate.parse(date), hours, ActionType.DELETE));
    }

    @When("the main service publishes a workload event with a blank username of {double} hours on {string}")
    public void publishInvalidEvent(double hours, String date) {
        publish(new TrainerWorkloadRequest("", "First", "Last", true,
                LocalDate.parse(date), hours, ActionType.ADD));
    }

    @Then("the trainer {string} eventually has {double} hours recorded for {int}-{int}")
    public void trainerEventuallyHasHours(String username, double hours, int year, int month) {
        await().atMost(Duration.ofSeconds(10)).untilAsserted(() ->
                mockMvc.perform(get("/api/trainers/workload/{username}", username)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TestTokens.tokenFor(username)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.years[0].year").value(year))
                        .andExpect(jsonPath("$.years[0].months[0].month").value(month))
                        .andExpect(jsonPath("$.years[0].months[0].summaryDuration").value(hours)));
    }

    @Then("the invalid event is moved to the dead letter queue")
    public void theInvalidEventIsMovedToTheDeadLetterQueue() {
        Object dead = jmsTemplate.receiveAndConvert(WorkloadMessaging.DEAD_LETTER_QUEUE);
        assertThat(dead).isInstanceOf(TrainerWorkloadRequest.class);
        assertThat(((TrainerWorkloadRequest) dead).username()).isBlank();
    }

    @Then("no workload is stored for trainer {string}")
    public void noWorkloadIsStoredForTrainer(String username) {
        assertThat(state.getStore()).doesNotContainKey(username);
    }

    private void publish(TrainerWorkloadRequest request) {
        jmsTemplate.convertAndSend(WorkloadMessaging.QUEUE, request, message -> {
            message.setStringProperty(WorkloadMessaging.TRANSACTION_ID_PROPERTY, "integration-tx");
            return message;
        });
    }
}
