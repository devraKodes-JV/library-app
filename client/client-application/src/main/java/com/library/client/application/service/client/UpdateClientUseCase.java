package com.library.client.application.service.client;

import com.library.client.application.dto.command.client.UpdateClientCommand;
import com.library.client.application.dto.response.client.ClientResponseDTO;
import com.library.client.application.validation.ClientValidator;
import com.library.client.domain.exception.ClientNotFoundException;
import com.library.client.domain.model.Client;
import com.library.client.domain.port.out.ClientRepository;

public class UpdateClientUseCase {

    private final ClientRepository clientRepository;
    private final ClientValidator clientValidator;

    public UpdateClientUseCase(ClientRepository clientRepository, ClientValidator clientValidator) {
        this.clientRepository = clientRepository;
        this.clientValidator = clientValidator;
    }

    public ClientResponseDTO execute(UpdateClientCommand command) {
        Client existing = clientRepository.findById(command.id())
                .orElseThrow(() -> new ClientNotFoundException(command.id()));

        Client updated = new Client(
                existing.getId(),
                existing.getCode(),
                existing.getDni(),
                command.fullName(),
                command.email(),
                command.phone(),
                command.address(),
                existing.getType(),
                existing.getStatus(),
                existing.getMemberSince(),
                existing.getMemberUntil(),
                existing.getBirthDate(),
                existing.isMembershipPaid(),
                command.notes(),
                existing.isEnabled(),
                existing.getCreatedAt(),
                existing.getUpdatedAt());

        clientValidator.validate(updated);
        Client saved = clientRepository.save(updated);
        return ClientResponseDTO.of(saved);
    }
}
