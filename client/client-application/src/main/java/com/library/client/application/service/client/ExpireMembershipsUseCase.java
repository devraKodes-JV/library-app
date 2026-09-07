package com.library.client.application.service.client;

import java.time.LocalDate;
import java.util.List;

import com.library.client.domain.model.Client;
import com.library.client.domain.model.ClientType;
import com.library.client.domain.port.out.ClientRepository;

public class ExpireMembershipsUseCase {

    private final ClientRepository clientRepository;

    public ExpireMembershipsUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public int execute() {
        List<Client> expired = clientRepository.findExpiredUnpaidMembers(LocalDate.now());
        for (Client c : expired) {
            c.setType(ClientType.CASUAL);
            c.setMemberUntil(null);
            c.setMembershipPaid(false);
            clientRepository.save(c);
        }
        return expired.size();
    }
}
