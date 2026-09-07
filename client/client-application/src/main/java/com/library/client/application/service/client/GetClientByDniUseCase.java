package com.library.client.application.service.client;

import com.library.client.application.dto.response.client.ClientDetailResponseDTO;
import com.library.client.domain.exception.ClientNotFoundException;
import com.library.client.domain.model.Client;
import com.library.client.domain.port.out.ClientRepository;

public class GetClientByDniUseCase {

    private final ClientRepository clientRepository;

    public GetClientByDniUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientDetailResponseDTO execute(String dni) {
        Client client = clientRepository.findByDni(dni)
                .orElseThrow(() -> new ClientNotFoundException("DNI not found: " + dni));
        return ClientDetailResponseDTO.of(client);
    }
}
