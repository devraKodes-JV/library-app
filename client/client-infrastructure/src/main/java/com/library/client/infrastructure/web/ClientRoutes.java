package com.library.client.infrastructure.web;

import com.library.client.infrastructure.web.controller.client.AjaxUpdateClientController;
import com.library.client.infrastructure.web.controller.client.CreateClientController;
import com.library.client.infrastructure.web.controller.client.DeleteClientController;
import com.library.client.infrastructure.web.controller.client.ListClientsController;
import com.library.client.infrastructure.web.controller.client.ReactivateClientController;
import com.library.client.infrastructure.web.controller.client.ShowClientController;
import com.library.client.infrastructure.web.controller.client.UpdateClientController;

import io.javalin.config.JavalinConfig;

public final class ClientRoutes {

    private ClientRoutes() {
    }

    public static void register(JavalinConfig config,
                                ListClientsController listClientsController,
                                ShowClientController showClientController,
                                CreateClientController createClientController,
                                UpdateClientController updateClientController,
                                DeleteClientController deleteClientController,
                                ReactivateClientController reactivateClientController,
                                AjaxUpdateClientController ajaxUpdateClientController) {

        config.routes.get("/clients", listClientsController::listClients);
        config.routes.get("/clients/new", createClientController::showCreateForm);
        config.routes.post("/clients", createClientController::createClient);
        config.routes.get("/clients/{id}", showClientController::showClient);
        config.routes.get("/clients/{id}/edit", updateClientController::showEditForm);
        config.routes.post("/clients/{id}", updateClientController::updateClient);
        config.routes.post("/clients/{id}/delete", deleteClientController::deleteClient);
        config.routes.post("/clients/{id}/reactivate", reactivateClientController::reactivateClient);

        config.routes.post("/api/clients/{id}/update", ajaxUpdateClientController::updateClientAjax);
    }
}
