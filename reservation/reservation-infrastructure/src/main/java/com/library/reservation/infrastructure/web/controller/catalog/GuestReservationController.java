package com.library.reservation.infrastructure.web.controller.catalog;

import java.util.Map;

import com.library.books.domain.model.Edition;
import com.library.books.domain.port.out.EditionRepository;
import com.library.client.domain.model.Client;
import com.library.client.domain.model.ClientStatus;
import com.library.client.domain.model.ClientType;
import com.library.client.domain.port.out.ClientRepository;
import com.library.kernel.loan.LoanPolicy;
import com.library.reservation.application.dto.command.reservation.CreateReservationCommand;
import com.library.reservation.application.service.reservation.CreateReservationUseCase;
import com.library.stock.domain.port.out.StockItemRepository;

import io.javalin.http.Context;

public class GuestReservationController {

    private final EditionRepository editionRepository;
    private final CreateReservationUseCase createReservationUseCase;
    private final ClientRepository clientRepository;
    private final StockItemRepository stockItemRepository;
    private final LoanPolicy loanPolicy;

    public GuestReservationController(EditionRepository editionRepository,
                                     CreateReservationUseCase createReservationUseCase,
                                     ClientRepository clientRepository,
                                     StockItemRepository stockItemRepository,
                                     LoanPolicy loanPolicy) {
        this.editionRepository = editionRepository;
        this.createReservationUseCase = createReservationUseCase;
        this.clientRepository = clientRepository;
        this.stockItemRepository = stockItemRepository;
        this.loanPolicy = loanPolicy;
    }

    public void showReserveForm(Context ctx) {
        long editionId = ctx.pathParamAsClass("editionId", Long.class).get();
        Edition edition = editionRepository.findById(editionId).orElse(null);
        if (edition == null) {
            ctx.redirect("/catalog");
            return;
        }

        ctx.render("reservation/catalog/reserve.html", Map.of(
                "editionId", editionId,
                "bookTitle", "Book #" + editionId,
                "editionNumber", edition.getEditionNumber() != null ? edition.getEditionNumber() : ""
        ));
    }

    public void createReservation(Context ctx) {
        long editionId = ctx.pathParamAsClass("editionId", Long.class).get();
        String dni = ctx.formParam("dni");
        Integer loanDays = parseInteger(ctx.formParam("loanDays"));

        if (dni == null || dni.isBlank()) {
            ctx.redirect("/catalog/reserve/" + editionId);
            return;
        }

        Client client = clientRepository.findByDni(dni).orElse(null);
        if (client == null) {
            client = createNewClient(dni);
        }

        int days = (loanDays != null && loanDays > 0) ? loanDays : 7;

        CreateReservationCommand command = new CreateReservationCommand(
                client.getId(),
                editionId,
                days,
                null
        );

        try {
            createReservationUseCase.execute(command);
            ctx.redirect("/catalog/reservation/success");
        } catch (Exception e) {
            ctx.redirect("/catalog/reserve/" + editionId);
        }
    }

    private Client createNewClient(String dni) {
        String code = "CLI-" + dni;
        Client client = Client.withoutId(
                code,
                dni,
                "",
                "",
                "",
                "",
                ClientType.CASUAL,
                ClientStatus.ACTIVE,
                java.time.LocalDate.now(),
                null,
                null,
                null);
        return clientRepository.save(client);
    }

    private Integer parseInteger(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
