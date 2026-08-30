package org.gym.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RestCallLoggingInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RestCallLoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("Endpoint called: {} {} query={}",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (ex == null) {
            log.info("Endpoint {} {} responded with status {}",
                    request.getMethod(), request.getRequestURI(), response.getStatus());
        } else {
            log.warn("Endpoint {} {} responded with status {} and error {}",
                    request.getMethod(), request.getRequestURI(), response.getStatus(), ex.getMessage());
        }
    }
}
