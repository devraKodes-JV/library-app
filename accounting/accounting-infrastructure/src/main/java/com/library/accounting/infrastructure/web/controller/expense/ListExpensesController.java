package com.library.accounting.infrastructure.web.controller.expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.library.accounting.application.dto.command.expense.CreateExpenseCommand;
import com.library.accounting.application.service.expense.CreateExpenseUseCase;
import com.library.accounting.application.service.expense.ListExpensesUseCase;
import com.library.accounting.domain.model.expense.Expense;
import com.library.accounting.domain.model.expense.ExpenseCategory;
import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class ListExpensesController extends BaseController {

    private final ListExpensesUseCase listExpensesUseCase;
    private final CreateExpenseUseCase createExpenseUseCase;

    public ListExpensesController(ListExpensesUseCase listExpensesUseCase,
                                  CreateExpenseUseCase createExpenseUseCase,
                                  WebControllerContext webContext) {
        super(webContext);
        this.listExpensesUseCase = listExpensesUseCase;
        this.createExpenseUseCase = createExpenseUseCase;
    }

    public void listExpenses(Context ctx) {
        requireCan(ctx, "accounting.expenses.read");
        List<Expense> expenses = listExpensesUseCase.execute();
        ctx.render("accounting/expenses/list", buildModel(ctx, Map.of(
                "expenses", expenses,
                "categories", ExpenseCategory.values(),
                "canCreate", hasPermission(ctx, "accounting.expenses.create"))));
    }

    public void showCreateForm(Context ctx) {
        requireCan(ctx, "accounting.expenses.create");
        ctx.render("accounting/expenses/form", buildModel(ctx, Map.of(
                "categories", ExpenseCategory.values())));
    }

    public void createExpense(Context ctx) {
        requireCan(ctx, "accounting.expenses.create");
        CreateExpenseCommand command = new CreateExpenseCommand(
                parseDate(ctx.formParam("expenseDate")),
                ctx.formParam("category"),
                ctx.formParam("description"),
                parseBigDecimal(ctx.formParam("amount")),
                ctx.formParam("paymentMethod"),
                ctx.formParam("vendor"),
                ctx.formParam("receiptNumber"),
                ctx.formParam("notes")
        );

        try {
            createExpenseUseCase.execute(command, currentUserName(ctx));
            flashSuccess(ctx, "Expense recorded successfully.");
            ctx.redirect("/accounting/expenses");
        } catch (IllegalArgumentException e) {
            ctx.render("accounting/expenses/form", buildModel(ctx, Map.of(
                    "categories", ExpenseCategory.values(),
                    "error", e.getMessage())));
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
