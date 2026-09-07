package com.library.client.application.service.client;

import com.library.client.application.dto.response.client.ClientDetailResponseDTO;
import com.library.client.domain.exception.ClientNotFoundException;
import com.library.client.domain.model.Client;
import com.library.client.domain.port.out.ClientRepository;

public class GetClientUseCase {

    private final ClientRepository clientRepository;

    public GetClientUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientDetailResponseDTO execute(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
        return ClientDetailResponseDTO.of(client);
    }
}
