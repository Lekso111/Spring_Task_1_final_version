package org.gym.workload.messaging;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.gym.workload.dto.TrainerWorkloadRequest;
import org.gym.workload.logging.TransactionContext;
import org.gym.workload.service.WorkloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class WorkloadMessageListener {

    private static final Logger log = LoggerFactory.getLogger(WorkloadMessageListener.class);

    private final WorkloadService workloadService;
    private final JmsTemplate jmsTemplate;
    private final Validator validator;

    public WorkloadMessageListener(WorkloadService workloadService, JmsTemplate jmsTemplate, Validator validator) {
        this.workloadService = workloadService;
        this.jmsTemplate = jmsTemplate;
        this.validator = validator;
    }

    @JmsListener(destination = WorkloadMessaging.QUEUE)
    public void onMessage(TrainerWorkloadRequest request,
                          @Header(name = WorkloadMessaging.TRANSACTION_ID_PROPERTY, required = false) String transactionId) {
        MDC.put(TransactionContext.TRANSACTION_ID, transactionId != null ? transactionId : UUID.randomUUID().toString());
        try {
            Set<ConstraintViolation<TrainerWorkloadRequest>> violations = validator.validate(request);
            if (!violations.isEmpty()) {
                String reason = violations.stream()
                        .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                        .collect(Collectors.joining(", "));
                log.warn("Invalid workload message, routing to dead letter queue: {}", reason);
                jmsTemplate.convertAndSend(WorkloadMessaging.DEAD_LETTER_QUEUE, request);
                return;
            }
            log.info("Received {} workload message for trainer {}", request.actionType(), request.username());
            workloadService.updateWorkload(request);
        } finally {
            MDC.remove(TransactionContext.TRANSACTION_ID);
        }
    }
}
