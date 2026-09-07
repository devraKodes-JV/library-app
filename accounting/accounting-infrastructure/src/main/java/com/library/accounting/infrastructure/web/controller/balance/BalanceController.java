package com.library.accounting.infrastructure.web.controller.balance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.library.accounting.application.service.balance.BalanceDTO;
import com.library.accounting.application.service.balance.GetBalanceUseCase;
import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class BalanceController extends BaseController {

    private final GetBalanceUseCase getBalanceUseCase;

    public BalanceController(GetBalanceUseCase getBalanceUseCase, WebControllerContext webContext) {
        super(webContext);
        this.getBalanceUseCase = getBalanceUseCase;
    }

    public void showBalance(Context ctx) {
        requireCan(ctx, "accounting.balance.read");
        String fromParam = ctx.queryParam("from");
        String toParam = ctx.queryParam("to");
        String methodParam = ctx.queryParam("method");

        LocalDate from = parseDate(fromParam, LocalDate.now().minusMonths(1));
        LocalDate to = parseDate(toParam, LocalDate.now());
        String method = methodParam != null && !methodParam.isBlank() ? methodParam : null;

        BalanceDTO balance = getBalanceUseCase.execute(from, to, method);

        ctx.render("accounting/balance", buildModel(ctx, Map.of(
                "balance", balance,
                "from", from,
                "to", to,
                "method", method != null ? method : ""
        )));
    }

    private LocalDate parseDate(String value, LocalDate defaultVal) {
        if (value == null || value.isBlank()) return defaultVal;
        try {
            return LocalDate.parse(value);
        } catch (Exception e) {
            return defaultVal;
        }
    }

    private Map<String, Object> buildModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> sections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", sections);
        model.putAll(extra);
        return model;
    }
}
