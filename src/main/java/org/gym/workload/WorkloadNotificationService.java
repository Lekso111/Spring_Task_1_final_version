package org.gym.workload;

import org.core.entities.Trainer;
import org.core.entities.Training;
import org.gym.logging.TransactionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
public class WorkloadNotificationService {

    private static final Logger log = LoggerFactory.getLogger(WorkloadNotificationService.class);

    private final WorkloadMessageProducer workloadMessageProducer;

    public WorkloadNotificationService(WorkloadMessageProducer workloadMessageProducer) {
        this.workloadMessageProducer = workloadMessageProducer;
    }

    public void notify(Training training, ActionType actionType) {
        Trainer trainer = training.getTrainer();
        TrainerWorkloadRequest request = new TrainerWorkloadRequest(
                trainer.getUserName(),
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.isActive(),
                training.getDate(),
                training.getDuration(),
                actionType);
        log.info("Notifying workload service with {} for trainer {}", actionType, trainer.getUserName());
        workloadMessageProducer.send(request, MDC.get(TransactionContext.TRANSACTION_ID));
    }
}
