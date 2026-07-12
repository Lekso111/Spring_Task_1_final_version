package org.gym.workload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WorkloadClientFallback implements WorkloadClient {

    private static final Logger log = LoggerFactory.getLogger(WorkloadClientFallback.class);

    @Override
    public void updateWorkload(TrainerWorkloadRequest request, String transactionId) {
        log.warn("Workload service is unavailable, skipped {} notification for trainer {}",
                request.actionType(), request.username());
    }
}
