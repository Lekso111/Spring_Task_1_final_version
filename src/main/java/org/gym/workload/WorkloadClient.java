package org.gym.workload;

import org.gym.logging.TransactionContext;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "trainer-workload-service",
        path = "/api/trainers/workload",
        configuration = WorkloadFeignConfig.class,
        fallback = WorkloadClientFallback.class)
public interface WorkloadClient {

    @PostMapping
    void updateWorkload(@RequestBody TrainerWorkloadRequest request,
                        @RequestHeader(name = TransactionContext.HEADER, required = false) String transactionId);
}
