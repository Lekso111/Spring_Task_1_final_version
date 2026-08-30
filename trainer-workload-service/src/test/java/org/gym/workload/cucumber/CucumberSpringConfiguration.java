package org.gym.workload.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import org.gym.workload.repository.TrainerWorkloadRepository;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

@CucumberContextConfiguration
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration,"
                        + "org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration,"
                        + "org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration,"
                        + "org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration",
                "eureka.client.enabled=false",
                "spring.cloud.discovery.enabled=false",
                "spring.cloud.service-registry.auto-registration.enabled=false",
                "security.jwt.secret=integration-and-component-test-secret-key-that-is-long-enough",
                "spring.artemis.mode=embedded",
                "spring.artemis.embedded.persistent=false",
                "spring.jms.template.receive-timeout=4s"
        })
@AutoConfigureMockMvc
public class CucumberSpringConfiguration {

    @MockBean
    TrainerWorkloadRepository trainerWorkloadRepository;

    @TestConfiguration
    static class ScenarioBeans {

        @Bean
        ScenarioState scenarioState() {
            return new ScenarioState();
        }
    }
}
