package com.library.client.application.service.client;

import com.library.client.application.dto.command.client.ReactivateClientCommand;
import com.library.client.domain.exception.ClientNotFoundException;
import com.library.client.domain.model.Client;
import com.library.client.domain.port.out.ClientRepository;

public class ReactivateClientUseCase {

    private final ClientRepository clientRepository;

    public ReactivateClientUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void execute(ReactivateClientCommand command) {
        if (clientRepository.findByIdIncludingDeleted(command.id()).isEmpty()) {
            throw new ClientNotFoundException(command.id());
        }
        clientRepository.reactivateById(command.id());
    }
}
