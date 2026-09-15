package com.library.accounting.infrastructure.web.controller.paymentmethod;

import com.library.accounting.application.service.paymentmethod.CreatePaymentMethodUseCase;
import com.library.accounting.application.service.paymentmethod.DeletePaymentMethodUseCase;
import com.library.accounting.application.service.paymentmethod.ListPaymentMethodsUseCase;
import com.library.accounting.domain.model.PaymentMethod;
import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import java.util.List;
import java.util.Map;

public class PaymentMethodController extends BaseController {

    private final ListPaymentMethodsUseCase listUseCase;
    private final CreatePaymentMethodUseCase createUseCase;
    private final DeletePaymentMethodUseCase deleteUseCase;

    public PaymentMethodController(ListPaymentMethodsUseCase listUseCase,
                                   CreatePaymentMethodUseCase createUseCase,
                                   DeletePaymentMethodUseCase deleteUseCase,
                                   WebControllerContext webContext) {
        super(webContext);
        this.listUseCase = listUseCase;
        this.createUseCase = createUseCase;
        this.deleteUseCase = deleteUseCase;
    }

    public void listPaymentMethods(Context ctx) {
        requireCan(ctx, "accounting.paymentmethods.read");
        List<PaymentMethod> methods = listUseCase.execute();
        ctx.render("accounting/paymentmethods/list", baseModel(ctx, Map.of(
                "paymentMethods", methods,
                "canCreate", hasPermission(ctx, "accounting.paymentmethods.create"),
                "canDelete", hasPermission(ctx, "accounting.paymentmethods.delete")
        )));
    }

    public void showCreateForm(Context ctx) {
        requireCan(ctx, "accounting.paymentmethods.create");
        ctx.render("accounting/paymentmethods/form", baseModel(ctx, Map.of(
                "action", "create",
                "paymentMethod", new PaymentMethod()
        )));
    }

    public void createPaymentMethod(Context ctx) {
        requireCan(ctx, "accounting.paymentmethods.create");
        String name = ctx.formParam("name");
        if (name == null || name.isBlank()) {
            flashDanger(ctx, "Name is required");
            ctx.redirect("/accounting/payment-methods/new");
            return;
        }
        createUseCase.execute(name, currentUserName(ctx));
        flashSuccess(ctx, "Payment method created");
        ctx.redirect("/accounting/payment-methods");
    }

    public void deletePaymentMethod(Context ctx) {
        requireCan(ctx, "accounting.paymentmethods.delete");
        Long id = Long.valueOf(ctx.pathParam("id"));
        deleteUseCase.execute(id);
        flashSuccess(ctx, "Payment method removed");
        ctx.redirect("/accounting/payment-methods");
    }
}
