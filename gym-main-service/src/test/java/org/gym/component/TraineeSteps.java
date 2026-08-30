package org.gym.component;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.core.entities.Trainee;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class TraineeSteps {

    private final ComponentTestContext context;

    public TraineeSteps(ComponentTestContext context) {
        this.context = context;
    }

    @Given("the username {string} is not taken yet")
    public void theUsernameIsNotTakenYet(String username) {
        when(context.usersRepository.existsByUsername(any())).thenReturn(false);
    }

    @Given("a trainee {string} named {string} {string} is registered")
    public void aTraineeIsRegistered(String username, String firstName, String lastName) {
        Trainee trainee = context.buildTrainee(username, firstName, lastName);
        when(context.traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));
    }

    @Given("no trainee is registered with username {string}")
    public void noTraineeIsRegisteredWithUsername(String username) {
        when(context.traineeRepository.findByUsername(username)).thenReturn(Optional.empty());
    }

    @When("a trainee is registered with first name {string} and last name {string}")
    public void aTraineeIsRegisteredWith(String firstName, String lastName) throws Exception {
        String body = "{\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\","
                + "\"dateOfBirth\":\"2000-01-01\",\"address\":\"221B Baker Street\"}";
        context.setLastResult(context.mockMvc.perform(post("/api/trainees/register")
                .contentType(APPLICATION_JSON)
                .content(body)));
    }

    @When("a trainee is registered with a blank first name")
    public void aTraineeIsRegisteredWithABlankFirstName() throws Exception {
        String body = "{\"firstName\":\"\",\"lastName\":\"doe\"}";
        context.setLastResult(context.mockMvc.perform(post("/api/trainees/register")
                .contentType(APPLICATION_JSON)
                .content(body)));
    }

    @When("the profile of trainee {string} is requested")
    public void theProfileOfTraineeIsRequested(String username) throws Exception {
        context.setLastResult(context.mockMvc.perform(get("/api/trainees/" + username)));
    }

    @Then("the returned username is {string}")
    public void theReturnedUsernameIs(String username) throws Exception {
        context.getLastResult().andExpect(jsonPath("$.username").value(username));
    }

    @Then("the response carries a generated password")
    public void theResponseCarriesAGeneratedPassword() throws Exception {
        context.getLastResult().andExpect(jsonPath("$.password").isNotEmpty());
    }

    @Then("the returned first name is {string}")
    public void theReturnedFirstNameIs(String firstName) throws Exception {
        context.getLastResult().andExpect(jsonPath("$.firstName").value(firstName));
    }
}
