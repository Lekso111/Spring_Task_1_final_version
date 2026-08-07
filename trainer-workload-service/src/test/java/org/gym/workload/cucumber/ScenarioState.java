package org.gym.workload.cucumber;

import org.gym.workload.model.TrainerWorkload;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

public class ScenarioState {

    private final Map<String, TrainerWorkload> store = new HashMap<>();

    private ResultActions lastResult;

    public Map<String, TrainerWorkload> getStore() {
        return store;
    }

    public ResultActions getLastResult() {
        return lastResult;
    }

    public void setLastResult(ResultActions lastResult) {
        this.lastResult = lastResult;
    }

    public void reset() {
        store.clear();
        lastResult = null;
    }
}
