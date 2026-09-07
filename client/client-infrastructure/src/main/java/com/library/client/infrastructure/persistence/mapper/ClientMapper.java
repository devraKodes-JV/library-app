package com.library.client.infrastructure.persistence.mapper;

import com.library.client.domain.model.Client;
import com.library.client.infrastructure.persistence.entity.ClientEntity;

public final class ClientMapper {

    private ClientMapper() {
    }

    public static Client toDomain(ClientEntity e) {
        if (e == null) {
            return null;
        }
        return new Client(
                e.getId(),
                e.getCode(),
                e.getDni(),
                e.getFullName(),
                e.getEmail(),
                e.getPhone(),
                e.getAddress(),
                e.getType(),
                e.getStatus(),
                e.getMemberSince(),
                e.getMemberUntil(),
                e.getBirthDate(),
                e.isMembershipPaid(),
                e.getNotes(),
                e.isEnabled(),
                e.getCreatedAt(),
                e.getUpdatedAt());
    }

    public static ClientEntity toEntity(Client c) {
        if (c == null) {
            return null;
        }
        return new ClientEntity(
                c.getId(),
                c.getCode(),
                c.getDni(),
                c.getFullName(),
                c.getEmail(),
                c.getPhone(),
                c.getAddress(),
                c.getType(),
                c.getStatus(),
                c.getMemberSince(),
                c.getMemberUntil(),
                c.getBirthDate(),
                c.isMembershipPaid(),
                c.getNotes(),
                c.isEnabled());
    }
}
