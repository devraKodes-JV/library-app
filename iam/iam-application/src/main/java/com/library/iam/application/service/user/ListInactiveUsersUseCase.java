package com.library.iam.application.service.user;

import java.util.List;

import com.library.iam.application.dto.UserDTO;
import com.library.iam.domain.port.out.UserPort;

public class ListInactiveUsersUseCase {

    private final UserPort userPort;

    public ListInactiveUsersUseCase(UserPort userPort) {
        this.userPort = userPort;
    }

    public List<UserDTO> execute() {
        return userPort.findInactive().stream()
                .map(UserDTO::of)
                .toList();
    }
}
