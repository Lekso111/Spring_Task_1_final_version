package org.gym.workload.messaging;

public final class WorkloadMessaging {

    public static final String QUEUE = "trainer.workload.queue";
    public static final String DEAD_LETTER_QUEUE = "trainer.workload.dlq";
    public static final String TYPE_ID = "workloadRequest";
    public static final String TRANSACTION_ID_PROPERTY = "transactionId";

    private WorkloadMessaging() {
    }
}
