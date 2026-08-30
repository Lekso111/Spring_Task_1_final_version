package org.gym.logging;

import org.slf4j.MDC;

public final class TransactionContext {

    public static final String TRANSACTION_ID = "transactionId";
    public static final String HEADER = "X-Transaction-Id";

    private TransactionContext() {
    }

    public static String getTransactionId() {
        return MDC.get(TRANSACTION_ID);
    }
}
