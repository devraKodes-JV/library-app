package com.library.client.application.dto.response.client;

import com.library.client.domain.model.Client;

import java.time.Instant;
import java.time.LocalDate;

public record ClientDetailResponseDTO(
        Long id,
        String code,
        String dni,
        String fullName,
        String email,
        String phone,
        String address,
        String type,
        String typeLabel,
        String status,
        String statusLabel,
        LocalDate memberSince,
        LocalDate memberUntil,
        LocalDate birthDate,
        String notes,
        boolean enabled,
        Instant createdAt,
        Instant updatedAt) {

    public static ClientDetailResponseDTO of(Client client) {
        return new ClientDetailResponseDTO(
                client.getId(),
                client.getCode(),
                client.getDni(),
                client.getFullName(),
                client.getEmail(),
                client.getPhone(),
                client.getAddress(),
                client.getType() != null ? client.getType().name() : null,
                client.getType() != null ? client.getType().getLabel() : null,
                client.getStatus() != null ? client.getStatus().name() : null,
                client.getStatus() != null ? client.getStatus().getLabel() : null,
                client.getMemberSince(),
                client.getMemberUntil(),
                client.getBirthDate(),
                client.getNotes(),
                client.isEnabled(),
                client.getCreatedAt(),
                client.getUpdatedAt());
    }
}
