package com.library.iam.application.service.password;

import com.library.iam.domain.port.out.PasswordHasher;

public class PasswordOperationsUseCase {

    private final PasswordHasher passwordHasher;

    public PasswordOperationsUseCase(PasswordHasher passwordHasher) {
        this.passwordHasher = passwordHasher;
    }

    public String hash(String rawPassword) {
        return passwordHasher.hash(rawPassword);
    }

    public boolean verify(String rawPassword, String encodedHash) {
        return passwordHasher.verify(rawPassword, encodedHash);
    }
}
