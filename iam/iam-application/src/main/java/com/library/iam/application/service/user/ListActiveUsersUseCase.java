package com.library.iam.application.service.user;

import java.util.List;

import com.library.iam.application.dto.UserDTO;
import com.library.iam.domain.port.out.UserPort;

public class ListActiveUsersUseCase {

    private final UserPort userPort;

    public ListActiveUsersUseCase(UserPort userPort) {
        this.userPort = userPort;
    }

    public List<UserDTO> execute() {
        return execute("active");
    }

    public List<UserDTO> execute(String status) {
        return userPort.findAll(status).stream()
                .map(UserDTO::of)
                .toList();
    }
}
