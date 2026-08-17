package com.library.iam.application.service.login;

import com.library.iam.domain.exception.UserNotFoundException;
import com.library.iam.domain.model.User;
import com.library.iam.domain.port.out.UserPort;

public class LoadUserByUsernameUseCase {

    private final UserPort userPort;

    public LoadUserByUsernameUseCase(UserPort userPort) {
        this.userPort = userPort;
    }

    public User execute(String username) {
        return userPort.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public void save(User user) {
        userPort.save(user);
    }
}
