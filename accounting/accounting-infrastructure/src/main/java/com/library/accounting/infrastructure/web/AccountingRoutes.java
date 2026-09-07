package com.library.accounting.infrastructure.web;

import com.library.accounting.infrastructure.web.controller.balance.BalanceController;
import com.library.accounting.infrastructure.web.controller.expense.ListExpensesController;
import com.library.accounting.infrastructure.web.controller.payment.ListPaymentsController;
import com.library.accounting.infrastructure.web.controller.payroll.ListPayrollController;

import io.javalin.config.JavalinConfig;

public final class AccountingRoutes {

    private AccountingRoutes() {}

    public static void register(JavalinConfig config,
                                ListPaymentsController listPaymentsController,
                                ListPayrollController listPayrollController,
                                ListExpensesController listExpensesController,
                                BalanceController balanceController) {

        config.routes.get("/accounting/payments", listPaymentsController::listPayments);
        config.routes.get("/accounting/payments/new", listPaymentsController::showCreateForm);
        config.routes.post("/accounting/payments", listPaymentsController::createPayment);

        config.routes.get("/accounting/payroll", listPayrollController::listPayroll);
        config.routes.get("/accounting/payroll/new", listPayrollController::showCreateForm);
        config.routes.post("/accounting/payroll", listPayrollController::createPayroll);

        config.routes.get("/accounting/expenses", listExpensesController::listExpenses);
        config.routes.get("/accounting/expenses/new", listExpensesController::showCreateForm);
        config.routes.post("/accounting/expenses", listExpensesController::createExpense);

        config.routes.get("/accounting/balance", balanceController::showBalance);
    }
}
