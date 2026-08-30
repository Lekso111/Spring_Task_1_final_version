package org.gym.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TransactionIdFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(TransactionIdFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String transactionId = UUID.randomUUID().toString();
        MDC.put(TransactionContext.TRANSACTION_ID, transactionId);
        response.setHeader(TransactionContext.HEADER, transactionId);
        try {
            log.info("Transaction started for {} {}", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(request, response);
            log.info("Transaction finished for {} {} with status {}",
                    request.getMethod(), request.getRequestURI(), response.getStatus());
        } finally {
            MDC.remove(TransactionContext.TRANSACTION_ID);
        }
    }
}
