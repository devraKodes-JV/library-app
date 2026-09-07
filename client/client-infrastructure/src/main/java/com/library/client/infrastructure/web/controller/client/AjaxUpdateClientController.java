package com.library.client.infrastructure.web.controller.client;

import com.library.client.domain.model.Client;
import com.library.client.domain.port.out.ClientRepository;
import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class AjaxUpdateClientController extends BaseController {

    private final ClientRepository clientRepository;

    public AjaxUpdateClientController(ClientRepository clientRepository,
                                      WebControllerContext webContext) {
        super(webContext);
        this.clientRepository = clientRepository;
    }

    public void updateClientAjax(Context ctx) {
        requireCan(ctx, "reservations.fulfill");
        long clientId = ctx.pathParamAsClass("id", Long.class).get();

        Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            ctx.status(404);
            return;
        }

        String fullName = ctx.formParam("fullName");
        String email = ctx.formParam("email");
        String phone = ctx.formParam("phone");
        String address = ctx.formParam("address");

        if (fullName != null && !fullName.isBlank()) client.setFullName(fullName);
        if (email != null) client.setEmail(email);
        if (phone != null) client.setPhone(phone);
        if (address != null) client.setAddress(address);

        clientRepository.save(client);
        ctx.status(200);
    }
}
