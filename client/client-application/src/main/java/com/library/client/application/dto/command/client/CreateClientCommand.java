package com.library.client.application.dto.command.client;

import com.library.client.domain.model.ClientType;

import java.time.LocalDate;

public record CreateClientCommand(
        String dni,
        String fullName,
        String email,
        String phone,
        String address,
        ClientType type,
        LocalDate memberUntil,
        LocalDate birthDate,
        String notes) {
}
