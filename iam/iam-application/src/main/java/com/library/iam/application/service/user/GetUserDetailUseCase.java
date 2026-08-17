package com.library.iam.application.service.user;

import java.util.List;

import com.library.iam.application.dto.RoleDTO;
import com.library.iam.application.dto.UserDetailDTO;
import com.library.iam.domain.exception.UserNotFoundException;
import com.library.iam.domain.model.User;
import com.library.iam.domain.port.out.RolePort;
import com.library.iam.domain.port.out.UserPort;

public class GetUserDetailUseCase {

    private final UserPort userPort;
    private final RolePort rolePort;

    public GetUserDetailUseCase(UserPort userPort, RolePort rolePort) {
        this.userPort = userPort;
        this.rolePort = rolePort;
    }

    public UserDetailDTO execute(Long id) {
        User user = userPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        List<RoleDTO> roles = rolePort.findAll().stream()
                .map(RoleDTO::of)
                .toList();
        return UserDetailDTO.from(user, roles);
    }
}
