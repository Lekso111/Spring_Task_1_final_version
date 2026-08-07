package org.gym.workload.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.gym.workload.dto.ActionType;
import org.gym.workload.dto.TrainerWorkloadRequest;
import org.gym.workload.messaging.WorkloadMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WorkloadComponentSteps {

    @Autowired
    private WorkloadMessageListener listener;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScenarioState state;

    @Given("trainer {string} has already recorded {double} hours on {string}")
    public void trainerHasAlreadyRecorded(String username, double hours, String date) {
        listener.onMessage(request(username, hours, date, ActionType.ADD), "seed-tx");
    }

    @When("an ADD workload event for trainer {string} of {double} hours on {string} is received")
    public void anAddWorkloadEventIsReceived(String username, double hours, String date) {
        listener.onMessage(request(username, hours, date, ActionType.ADD), "component-tx");
    }

    @When("a DELETE workload event for trainer {string} of {double} hours on {string} is received")
    public void aDeleteWorkloadEventIsReceived(String username, double hours, String date) {
        listener.onMessage(request(username, hours, date, ActionType.DELETE), "component-tx");
    }

    @When("the workload summary for {string} is requested")
    public void theWorkloadSummaryIsRequested(String username) throws Exception {
        state.setLastResult(mockMvc.perform(get("/api/trainers/workload/{username}", username)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TestTokens.tokenFor(username))));
    }

    @Then("the monthly summary for {string} reports {double} hours for {int}-{int}")
    public void theMonthlySummaryReports(String username, double hours, int year, int month) throws Exception {
        mockMvc.perform(get("/api/trainers/workload/{username}", username)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TestTokens.tokenFor(username)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.years[0].year").value(year))
                .andExpect(jsonPath("$.years[0].months[0].month").value(month))
                .andExpect(jsonPath("$.years[0].months[0].summaryDuration").value(hours));
    }

    @Then("the summary is empty and the trainer is marked inactive")
    public void theSummaryIsEmptyAndInactive() throws Exception {
        state.getLastResult()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.years").isEmpty());
    }

    private TrainerWorkloadRequest request(String username, double hours, String date, ActionType actionType) {
        return new TrainerWorkloadRequest(username, "First", "Last", true,
                LocalDate.parse(date), hours, actionType);
    }
}
