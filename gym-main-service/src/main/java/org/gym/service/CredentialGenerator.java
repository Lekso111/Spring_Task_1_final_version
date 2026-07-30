package org.gym.service;

import org.core.Utilities.PasswordGenerator;
import org.gym.repository.UsersRepository;
import org.springframework.stereotype.Component;

@Component
public class CredentialGenerator {

    private final UsersRepository usersRepository;

    public CredentialGenerator(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public String generateUsername(String firstName, String lastName) {
        String base = firstName + "." + lastName;
        int serial = 1;
        String candidate = base + serial;
        while (usersRepository.existsByUsername(candidate)) {
            serial++;
            candidate = base + serial;
        }
        return candidate;
    }

    public String generatePassword() {
        return PasswordGenerator.generatePassword();
    }
}
