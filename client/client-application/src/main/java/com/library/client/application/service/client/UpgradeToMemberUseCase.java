package com.library.client.application.service.client;

import com.library.client.domain.exception.ClientNotFoundException;
import com.library.client.domain.model.Client;
import com.library.client.domain.model.ClientStatus;
import com.library.client.domain.model.ClientType;
import com.library.client.domain.port.out.ClientRepository;

import java.time.LocalDate;

public class UpgradeToMemberUseCase {
    private final ClientRepository clientRepository;

    public UpgradeToMemberUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void execute(Long id, LocalDate memberSince, LocalDate memberUntil) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
        client.setType(ClientType.MEMBER);
        client.setStatus(ClientStatus.ACTIVE);
        client.setMemberSince(memberSince);
        client.setMemberUntil(memberUntil);
        client.setMembershipPaid(true);
        clientRepository.save(client);
    }
}
