package org.gym.workload.cucumber;

import io.cucumber.java.Before;
import org.gym.workload.model.TrainerWorkload;
import org.gym.workload.repository.TrainerWorkloadRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class WorkloadHooks {

    @Autowired
    private TrainerWorkloadRepository repository;

    @Autowired
    private ScenarioState state;

    @Before
    public void prepareScenario() {
        state.reset();
        reset(repository);
        when(repository.findByUsername(anyString())).thenAnswer(invocation ->
                Optional.ofNullable(state.getStore().get(invocation.<String>getArgument(0))));
        when(repository.save(any(TrainerWorkload.class))).thenAnswer(invocation -> {
            TrainerWorkload workload = invocation.getArgument(0);
            state.getStore().put(workload.getUsername(), workload);
            return workload;
        });
    }
}
