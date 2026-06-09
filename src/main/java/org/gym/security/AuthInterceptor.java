package org.gym.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.gym.service.AuthenticationService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    public static final String USERNAME_HEADER = "X-Auth-Username";
    public static final String PASSWORD_HEADER = "X-Auth-Password";

    private final AuthenticationService authenticationService;

    public AuthInterceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String username = request.getHeader(USERNAME_HEADER);
        String password = request.getHeader(PASSWORD_HEADER);
        authenticationService.authenticate(username, password);
        return true;
    }
}
