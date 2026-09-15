package com.library.accounting.infrastructure.web.controller.payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.library.accounting.application.dto.command.payment.CreatePaymentCommand;
import com.library.accounting.application.dto.response.payment.PaymentResponseDTO;
import com.library.accounting.application.service.payment.CreatePaymentUseCase;
import com.library.accounting.application.service.paymentmethod.ListPaymentMethodsUseCase;
import com.library.accounting.application.service.payment.ListPaymentsUseCase;
import com.library.accounting.domain.model.Payment;
import com.library.accounting.infrastructure.persistence.mapper.PaymentMapper;
import com.library.client.application.dto.response.client.ClientResponseDTO;
import com.library.client.application.service.client.ListClientsUseCase;
import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class ListPaymentsController extends BaseController {

    private final ListPaymentsUseCase listPaymentsUseCase;
    private final ListClientsUseCase listClientsUseCase;
    private final CreatePaymentUseCase createPaymentUseCase;
    private final ListPaymentMethodsUseCase listPaymentMethodsUseCase;

    public ListPaymentsController(ListPaymentsUseCase listPaymentsUseCase,
                                   ListClientsUseCase listClientsUseCase,
                                   CreatePaymentUseCase createPaymentUseCase,
                                   ListPaymentMethodsUseCase listPaymentMethodsUseCase,
                                   WebControllerContext webContext) {
        super(webContext);
        this.listPaymentsUseCase = listPaymentsUseCase;
        this.listClientsUseCase = listClientsUseCase;
        this.createPaymentUseCase = createPaymentUseCase;
        this.listPaymentMethodsUseCase = listPaymentMethodsUseCase;
    }

    public void listPayments(Context ctx) {
        requireCan(ctx, "accounting.payments.read");
        List<Payment> payments = listPaymentsUseCase.execute();
        List<PaymentResponseDTO> paymentDTOs = payments.stream()
                .map(PaymentMapper::toDTO)
                .toList();
        ctx.render("accounting/payments/list", buildListModel(ctx, Map.of(
                "payments", paymentDTOs,
                "canCreate", hasPermission(ctx, "accounting.payments.create"))));
    }

    public void showCreateForm(Context ctx) {
        requireCan(ctx, "accounting.payments.create");
        List<ClientResponseDTO> clients = listClientsUseCase.execute("ACTIVE");
        ctx.render("accounting/payments/form", buildListModel(ctx, Map.of(
                "clients", clients,
                "paymentMethods", listPaymentMethodsUseCase.execute())));
    }

    public void createPayment(Context ctx) {
        requireCan(ctx, "accounting.payments.create");
        CreatePaymentCommand command = new CreatePaymentCommand(
                parseDate(ctx.formParam("paymentDate")),
                parseBigDecimal(ctx.formParam("amount")),
                ctx.formParam("paymentMethod"),
                ctx.formParam("category"),
                parseLong(ctx.formParam("clientId")),
                "MANUAL",
                null,
                ctx.formParam("notes")
        );

        try {
            createPaymentUseCase.execute(command, currentUserName(ctx));
            flashSuccess(ctx, "Payment recorded successfully.");
            ctx.redirect("/accounting/payments");
        } catch (IllegalArgumentException e) {
            List<ClientResponseDTO> clients = listClientsUseCase.execute("ACTIVE");
            ctx.render("accounting/payments/form", buildListModel(ctx, Map.of(
                    "clients", clients,
                    "paymentMethods", listPaymentMethodsUseCase.execute(),
                    "error", e.getMessage())));
        }
    }

    private Map<String, Object> buildListModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.putAll(extra);
        return model;
    }

    private Long parseLong(String value) {
        if (value == null || value.isBlank() || "0".equals(value)) return null;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) return LocalDate.now();
        try {
            return LocalDate.parse(value);
        } catch (Exception e) {
            return LocalDate.now();
        }
    }
}
