package org.gym.service;

import org.core.entities.User;
import org.gym.exception.AuthenticationException;
import org.gym.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {

    private final UsersRepository usersRepository;
    private final AuthenticationService authenticationService;

    public LoginService(UsersRepository usersRepository, AuthenticationService authenticationService) {
        this.usersRepository = usersRepository;
        this.authenticationService = authenticationService;
    }

    @Transactional(readOnly = true)
    public void login(String username, String password) {
        authenticationService.authenticate(username, password);
    }

    @Transactional
    public void changeLogin(String username, String oldPassword, String newPassword) {
        User user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));
        if (!user.getPassword().equals(oldPassword)) {
            throw new AuthenticationException("Invalid username or password");
        }
        user.setPassword(newPassword);
        usersRepository.save(user);
    }
}
