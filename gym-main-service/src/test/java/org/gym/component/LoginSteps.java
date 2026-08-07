package org.gym.component;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class LoginSteps {

    private final ComponentTestContext context;

    public LoginSteps(ComponentTestContext context) {
        this.context = context;
    }

    @Given("the credentials for {string} are valid")
    public void theCredentialsAreValid(String username) {
        when(context.authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(username, null));
    }

    @Given("the credentials for {string} are invalid")
    public void theCredentialsAreInvalid(String username) {
        when(context.authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));
    }

    @When("{string} logs in with password {string}")
    public void logsInWithPassword(String username, String password) throws Exception {
        String body = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        context.setLastResult(context.mockMvc.perform(post("/api/login")
                .contentType(APPLICATION_JSON)
                .content(body)));
    }

    @Then("the response carries a token")
    public void theResponseCarriesAToken() throws Exception {
        context.getLastResult().andExpect(jsonPath("$.token").isNotEmpty());
    }
}
