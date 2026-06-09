package org.gym.service;

import org.core.entities.Users;
import org.gym.exception.AuthenticationException;
import org.gym.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {

    private final UsersRepository usersRepository;

    public AuthenticationService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Transactional(readOnly = true)
    public void authenticate(String username, String password) {
        if (username == null || password == null) {
            throw new AuthenticationException("Username and password are required");
        }
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));
        if (!user.getPassword().equals(password)) {
            throw new AuthenticationException("Invalid username or password");
        }
    }
}
