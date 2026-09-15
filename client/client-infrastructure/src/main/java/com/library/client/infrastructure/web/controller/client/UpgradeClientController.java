package com.library.client.infrastructure.web.controller.client;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.library.accounting.application.service.payment.RecordClientPaymentUseCase;
import com.library.client.application.service.client.UpgradeToMemberUseCase;
import com.library.config.application.service.ConfigService;
import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class UpgradeClientController extends BaseController {

    private final UpgradeToMemberUseCase upgradeToMemberUseCase;
    private final RecordClientPaymentUseCase recordPaymentUseCase;
    private final ConfigService configService;

    public UpgradeClientController(UpgradeToMemberUseCase upgradeToMemberUseCase,
                                     RecordClientPaymentUseCase recordPaymentUseCase,
                                     ConfigService configService,
                                     WebControllerContext webContext) {
        super(webContext);
        this.upgradeToMemberUseCase = upgradeToMemberUseCase;
        this.recordPaymentUseCase = recordPaymentUseCase;
        this.configService = configService;
    }

    public void upgrade(Context ctx) {
        requireCan(ctx, "clients.upgrade");
        Long id = ctx.pathParamAsClass("id", Long.class).get();

        String paymentConfirmed = ctx.formParam("paymentConfirmed");
        if (!"true".equals(paymentConfirmed)) {
            flashDanger(ctx, "Payment must be confirmed to upgrade the client.");
            ctx.redirect("/clients");
            return;
        }

        int months = ctx.formParamAsClass("months", Integer.class).getOrDefault(12);

        String paymentMethod = ctx.formParam("paymentMethod");
        if (paymentMethod == null || paymentMethod.isBlank()) {
            paymentMethod = "CASH";
        }

        String currency = configService.get("accounting", "currency", "USD");
        String feeStr = months == 12
                ? configService.get("client", "membership.fee.yearly", "500.00")
                : configService.get("client", "membership.fee.monthly", "50.00");
        BigDecimal fee = new BigDecimal(feeStr);

        LocalDate memberSince = LocalDate.now();
        LocalDate memberUntil = memberSince.plusMonths(months);
        upgradeToMemberUseCase.execute(id, memberSince, memberUntil);

        recordPaymentUseCase.execute(
                fee,
                paymentMethod,
                "MEMBERSHIP",
                id,
                "CLIENT_UPGRADE",
                id,
                months == 12 ? "12-month membership" : "1-month membership",
                currentUserName(ctx));

        flashSuccess(ctx, "Client upgraded to MEMBER (" + currency + " " + fee + " via " + paymentMethod + ").");
        ctx.redirect("/clients");
    }
}
