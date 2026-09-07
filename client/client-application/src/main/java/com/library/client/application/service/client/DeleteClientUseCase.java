package com.library.client.application.service.client;

import com.library.client.application.dto.command.client.DeleteClientCommand;
import com.library.client.domain.exception.ClientNotFoundException;
import com.library.client.domain.model.Client;
import com.library.client.domain.port.out.ClientRepository;

public class DeleteClientUseCase {

    private final ClientRepository clientRepository;

    public DeleteClientUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void execute(DeleteClientCommand command) {
        Client existing = clientRepository.findById(command.id())
                .orElseThrow(() -> new ClientNotFoundException(command.id()));
        clientRepository.deleteById(existing.getId());
    }
}
