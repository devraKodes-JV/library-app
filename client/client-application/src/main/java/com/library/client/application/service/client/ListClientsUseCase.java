package com.library.client.application.service.client;

import com.library.client.application.dto.response.client.ClientResponseDTO;
import com.library.client.domain.port.out.ClientRepository;

import java.util.List;

public class ListClientsUseCase {

    private final ClientRepository clientRepository;

    public ListClientsUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<ClientResponseDTO> execute() {
        return clientRepository.findAll().stream()
                .map(ClientResponseDTO::of)
                .toList();
    }

    public List<ClientResponseDTO> execute(String status) {
        return clientRepository.findAll(status).stream()
                .map(ClientResponseDTO::of)
                .toList();
    }
}
