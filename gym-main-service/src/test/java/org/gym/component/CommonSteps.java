package org.gym.component;

import io.cucumber.java.en.Then;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommonSteps {

    private final ComponentTestContext context;

    public CommonSteps(ComponentTestContext context) {
        this.context = context;
    }

    @Then("the response status is {int}")
    public void theResponseStatusIs(int expectedStatus) throws Exception {
        context.getLastResult().andExpect(status().is(expectedStatus));
    }
}
