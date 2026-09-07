package com.library.client.application.service.client;

import com.library.client.application.dto.command.client.CreateClientCommand;
import com.library.client.application.dto.response.client.ClientResponseDTO;
import com.library.client.application.validation.ClientValidator;
import com.library.client.domain.model.Client;
import com.library.client.domain.model.ClientStatus;
import com.library.client.domain.port.out.ClientRepository;
import com.library.kernel.generation.CodeGenerationService;

import java.time.LocalDate;

public class CreateClientUseCase {

    private final ClientRepository clientRepository;
    private final ClientValidator clientValidator;
    private final CodeGenerationService codeGenerationService;

    public CreateClientUseCase(ClientRepository clientRepository, ClientValidator clientValidator,
                               CodeGenerationService codeGenerationService) {
        this.clientRepository = clientRepository;
        this.clientValidator = clientValidator;
        this.codeGenerationService = codeGenerationService;
    }

    public ClientResponseDTO execute(CreateClientCommand command) {
        String code = codeGenerationService.generate("CLI");
        while (clientRepository.findByCode(code).isPresent()) {
            code = codeGenerationService.generate("CLI");
        }

        Client client = Client.withoutId(
                code,
                command.dni(),
                command.fullName(),
                command.email(),
                command.phone(),
                command.address(),
                command.type(),
                ClientStatus.ACTIVE,
                LocalDate.now(),
                command.memberUntil(),
                command.birthDate(),
                command.notes());

        clientValidator.validate(client);
        Client saved = clientRepository.save(client);
        return ClientResponseDTO.of(saved);
    }
}
