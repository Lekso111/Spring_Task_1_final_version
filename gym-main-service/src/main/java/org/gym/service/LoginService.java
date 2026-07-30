package org.gym.service;

import org.core.entities.User;
import org.gym.dto.LoginResponse;
import org.gym.exception.AuthenticationException;
import org.gym.repository.UsersRepository;
import org.gym.security.JwtService;
import org.gym.security.LoginAttemptService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {

    private final UsersRepository usersRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final LoginAttemptService loginAttemptService;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UsersRepository usersRepository,
                        AuthenticationManager authenticationManager,
                        JwtService jwtService,
                        LoginAttemptService loginAttemptService,
                        PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.loginAttemptService = loginAttemptService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(String username, String password) {
        if (loginAttemptService.isBlocked(username)) {
            throw new AuthenticationException("Account is locked due to too many failed attempts. Try again in a few minutes.");
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (org.springframework.security.core.AuthenticationException ex) {
            loginAttemptService.loginFailed(username);
            throw new AuthenticationException("Invalid username or password");
        }
        loginAttemptService.loginSucceeded(username);
        return new LoginResponse(jwtService.generateToken(username));
    }

    @Transactional
    public void changeLogin(String username, String oldPassword, String newPassword) {
        User user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        usersRepository.save(user);
    }
}
