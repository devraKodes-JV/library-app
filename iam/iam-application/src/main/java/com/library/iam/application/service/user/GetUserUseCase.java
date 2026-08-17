package com.library.iam.application.service.user;

import com.library.iam.application.dto.UserDTO;
import com.library.iam.domain.exception.UserNotFoundException;
import com.library.iam.domain.port.out.UserPort;

public class GetUserUseCase {

    private final UserPort userPort;

    public GetUserUseCase(UserPort userPort) {
        this.userPort = userPort;
    }

    public UserDTO execute(Long id) {
        var user = userPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return UserDTO.of(user);
    }
}
