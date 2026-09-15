package com.library.accounting.infrastructure.web.controller.expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.library.accounting.application.dto.command.expense.CreateExpenseCommand;
import com.library.accounting.application.dto.response.expense.ExpenseResponseDTO;
import com.library.accounting.application.service.expense.CreateExpenseUseCase;
import com.library.accounting.application.service.expense.ListExpensesUseCase;
import com.library.accounting.application.service.paymentmethod.ListPaymentMethodsUseCase;
import com.library.accounting.domain.model.expense.Expense;
import com.library.accounting.domain.model.expense.ExpenseCategory;
import com.library.accounting.infrastructure.persistence.mapper.ExpenseMapper;
import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class ListExpensesController extends BaseController {

    private final ListExpensesUseCase listExpensesUseCase;
    private final CreateExpenseUseCase createExpenseUseCase;
    private final ListPaymentMethodsUseCase listPaymentMethodsUseCase;

    public ListExpensesController(ListExpensesUseCase listExpensesUseCase,
                                   CreateExpenseUseCase createExpenseUseCase,
                                   ListPaymentMethodsUseCase listPaymentMethodsUseCase,
                                   WebControllerContext webContext) {
        super(webContext);
        this.listExpensesUseCase = listExpensesUseCase;
        this.createExpenseUseCase = createExpenseUseCase;
        this.listPaymentMethodsUseCase = listPaymentMethodsUseCase;
    }

    public void listExpenses(Context ctx) {
        requireCan(ctx, "accounting.expenses.read");
        List<Expense> expenses = listExpensesUseCase.execute();
        List<ExpenseResponseDTO> expenseDTOs = expenses.stream()
                .map(ExpenseMapper::toDTO)
                .toList();
        ctx.render("accounting/expenses/list", buildListModel(ctx, Map.of(
                "expenses", expenseDTOs,
                "categories", ExpenseCategory.values(),
                "canCreate", hasPermission(ctx, "accounting.expenses.create"))));
    }

    public void showCreateForm(Context ctx) {
        requireCan(ctx, "accounting.expenses.create");
        ctx.render("accounting/expenses/form", buildListModel(ctx, Map.of(
                "categories", ExpenseCategory.values(),
                "paymentMethods", listPaymentMethodsUseCase.execute())));
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
            ctx.render("accounting/expenses/form", buildListModel(ctx, Map.of(
                    "categories", ExpenseCategory.values(),
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
