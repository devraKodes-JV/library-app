package com.library.bootstrap.factory;

import org.hibernate.SessionFactory;

import com.library.client.application.service.client.CreateClientUseCase;
import com.library.client.application.service.client.DeleteClientUseCase;
import com.library.client.application.service.client.GetClientBenefitsUseCase;
import com.library.client.application.service.client.GetClientUseCase;
import com.library.client.application.service.client.ListClientsUseCase;
import com.library.client.application.service.client.ReactivateClientUseCase;
import com.library.client.application.service.client.UpdateClientUseCase;
import com.library.client.application.validation.ClientValidator;
import com.library.client.domain.port.out.ClientRepository;
import com.library.client.infrastructure.loan.ClientLoanPolicy;
import com.library.client.infrastructure.persistence.adapter.ClientPersistenceAdapter;
import com.library.client.infrastructure.persistence.repository.hibernate.HibernateClientRepository;
import com.library.client.infrastructure.web.ClientRoutes;
import com.library.client.infrastructure.web.controller.client.AjaxUpdateClientController;
import com.library.client.infrastructure.web.controller.client.CreateClientController;
import com.library.client.infrastructure.web.controller.client.DeleteClientController;
import com.library.client.infrastructure.web.controller.client.ListClientsController;
import com.library.client.infrastructure.web.controller.client.ReactivateClientController;
import com.library.client.infrastructure.web.controller.client.ShowClientController;
import com.library.client.infrastructure.web.controller.client.UpdateClientController;
import com.library.kernel.generation.CodeGenerationService;
import com.library.bootstrap.generation.ShortUuidCodeGenerationService;
import com.library.kernel.loan.LoanPolicy;
import com.library.kernel.web.WebControllerContext;

import io.javalin.config.JavalinConfig;

public final class ClientFactory {

    private ClientFactory() {
    }

    public static ClientRepository clientRepository(SessionFactory sessionFactory) {
        return new ClientPersistenceAdapter(new HibernateClientRepository(sessionFactory),
                new HibernateClientRepository(sessionFactory));
    }

    public static void register(JavalinConfig config,
                                SessionFactory sessionFactory,
                                WebControllerContext webContext) {

        ClientRepository clientRepository = clientRepository(sessionFactory);

        CodeGenerationService codeGenerationService = new ShortUuidCodeGenerationService();
        ClientValidator clientValidator = new ClientValidator();

        LoanPolicy loanPolicy = new ClientLoanPolicy();
        GetClientBenefitsUseCase getClientBenefitsUseCase = new GetClientBenefitsUseCase(loanPolicy);

        CreateClientUseCase createClientUseCase = new CreateClientUseCase(clientRepository, clientValidator, codeGenerationService);
        UpdateClientUseCase updateClientUseCase = new UpdateClientUseCase(clientRepository, clientValidator);
        DeleteClientUseCase deleteClientUseCase = new DeleteClientUseCase(clientRepository);
        GetClientUseCase getClientUseCase = new GetClientUseCase(clientRepository);
        ListClientsUseCase listClientsUseCase = new ListClientsUseCase(clientRepository);
        ReactivateClientUseCase reactivateClientUseCase = new ReactivateClientUseCase(clientRepository);

        ListClientsController listClientsController = new ListClientsController(listClientsUseCase, webContext);
        ShowClientController showClientController = new ShowClientController(getClientUseCase, getClientBenefitsUseCase, webContext);
        CreateClientController createClientController = new CreateClientController(createClientUseCase, listClientsUseCase, webContext);
        UpdateClientController updateClientController = new UpdateClientController(updateClientUseCase, getClientUseCase, listClientsUseCase, webContext);
        DeleteClientController deleteClientController = new DeleteClientController(deleteClientUseCase, webContext);
        ReactivateClientController reactivateClientController = new ReactivateClientController(reactivateClientUseCase, webContext);
        AjaxUpdateClientController ajaxUpdateClientController = new AjaxUpdateClientController(clientRepository, webContext);

        ClientRoutes.register(config,
                listClientsController,
                showClientController,
                createClientController,
                updateClientController,
                deleteClientController,
                reactivateClientController,
                ajaxUpdateClientController);
    }
}
