package org.gym.workload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class WorkloadMessageProducer {

    private static final Logger log = LoggerFactory.getLogger(WorkloadMessageProducer.class);

    private final JmsTemplate jmsTemplate;

    public WorkloadMessageProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void send(TrainerWorkloadRequest request, String transactionId) {
        jmsTemplate.convertAndSend(WorkloadMessaging.QUEUE, request, message -> {
            if (transactionId != null) {
                message.setStringProperty(WorkloadMessaging.TRANSACTION_ID_PROPERTY, transactionId);
            }
            return message;
        });
        log.info("Published {} workload message for trainer {} to queue {}",
                request.actionType(), request.username(), WorkloadMessaging.QUEUE);
    }
}
