package com.library.bootstrap.factory;

import org.hibernate.SessionFactory;

import com.library.accounting.application.service.account.ListAccountsUseCase;
import com.library.accounting.application.service.balance.GetBalanceUseCase;
import com.library.accounting.application.service.expense.CreateExpenseUseCase;
import com.library.accounting.application.service.expense.ListExpensesUseCase;
import com.library.accounting.application.service.payment.CreatePaymentUseCase;
import com.library.accounting.application.service.payment.ListPaymentsUseCase;
import com.library.accounting.application.service.payroll.CreatePayrollUseCase;
import com.library.accounting.application.service.payroll.ListPayrollUseCase;
import com.library.accounting.domain.port.out.AccountRepository;
import com.library.accounting.domain.port.out.ExpenseRepository;
import com.library.accounting.domain.port.out.PaymentRepository;
import com.library.accounting.domain.port.out.PayrollRepository;
import com.library.accounting.domain.port.out.RefundRepository;
import com.library.accounting.infrastructure.persistence.adapter.AccountPersistenceAdapter;
import com.library.accounting.infrastructure.persistence.adapter.ExpensePersistenceAdapter;
import com.library.accounting.infrastructure.persistence.adapter.PaymentPersistenceAdapter;
import com.library.accounting.infrastructure.persistence.adapter.PayrollPersistenceAdapter;
import com.library.accounting.infrastructure.persistence.adapter.RefundPersistenceAdapter;
import com.library.accounting.infrastructure.persistence.repository.hibernate.HibernateAccountRepository;
import com.library.accounting.infrastructure.persistence.repository.hibernate.HibernateExpenseRepository;
import com.library.accounting.infrastructure.persistence.repository.hibernate.HibernatePaymentRepository;
import com.library.accounting.infrastructure.persistence.repository.hibernate.HibernatePayrollRepository;
import com.library.accounting.infrastructure.persistence.repository.hibernate.HibernateRefundRepository;
import com.library.accounting.infrastructure.web.AccountingRoutes;
import com.library.accounting.infrastructure.web.controller.balance.BalanceController;
import com.library.accounting.infrastructure.web.controller.expense.ListExpensesController;
import com.library.accounting.infrastructure.web.controller.payment.ListPaymentsController;
import com.library.accounting.infrastructure.web.controller.payroll.ListPayrollController;
import com.library.bootstrap.generation.ShortUuidCodeGenerationService;
import com.library.client.application.service.client.ListClientsUseCase;
import com.library.client.domain.port.out.ClientRepository;
import com.library.iam.application.service.user.ListActiveUsersUseCase;
import com.library.kernel.generation.CodeGenerationService;
import com.library.kernel.web.WebControllerContext;

import io.javalin.config.JavalinConfig;

public final class AccountingFactory {

    private AccountingFactory() {}

    public static void register(JavalinConfig config,
                                SessionFactory sessionFactory,
                                WebControllerContext webContext,
                                ClientRepository clientRepository) {

        // Repositories
        AccountRepository accountRepository = new AccountPersistenceAdapter(
                new HibernateAccountRepository(sessionFactory));
        PaymentRepository paymentRepository = new PaymentPersistenceAdapter(
                new HibernatePaymentRepository(sessionFactory));
        PayrollRepository payrollRepository = new PayrollPersistenceAdapter(
                new HibernatePayrollRepository(sessionFactory));
        ExpenseRepository expenseRepository = new ExpensePersistenceAdapter(
                new HibernateExpenseRepository(sessionFactory));
        @SuppressWarnings("unused")
        RefundRepository refundRepository = new RefundPersistenceAdapter(
                new HibernateRefundRepository(sessionFactory));

        // Code generation
        CodeGenerationService codeGenerationService = new ShortUuidCodeGenerationService();

        // Use cases
        ListAccountsUseCase listAccountsUseCase = new ListAccountsUseCase(accountRepository);
        ListPaymentsUseCase listPaymentsUseCase = new ListPaymentsUseCase(paymentRepository);
        CreatePaymentUseCase createPaymentUseCase = new CreatePaymentUseCase(
                paymentRepository, codeGenerationService);
        ListPayrollUseCase listPayrollUseCase = new ListPayrollUseCase(payrollRepository);
        CreatePayrollUseCase createPayrollUseCase = new CreatePayrollUseCase(
                payrollRepository, codeGenerationService);
        ListExpensesUseCase listExpensesUseCase = new ListExpensesUseCase(expenseRepository);
        CreateExpenseUseCase createExpenseUseCase = new CreateExpenseUseCase(
                expenseRepository, codeGenerationService);

        GetBalanceUseCase getBalanceUseCase = new GetBalanceUseCase(
                paymentRepository, expenseRepository, payrollRepository);

        // Cross-module use cases
        ListClientsUseCase listClientsUseCase = new ListClientsUseCase(clientRepository);
        ListActiveUsersUseCase listActiveUsersUseCase = new ListActiveUsersUseCase(
                com.library.bootstrap.factory.IamFactory.userPort(sessionFactory));

        // Controllers
        ListPaymentsController listPaymentsController = new ListPaymentsController(
                listPaymentsUseCase, listClientsUseCase, createPaymentUseCase, webContext);
        ListPayrollController listPayrollController = new ListPayrollController(
                listPayrollUseCase, listActiveUsersUseCase, createPayrollUseCase, webContext);
        ListExpensesController listExpensesController = new ListExpensesController(
                listExpensesUseCase, createExpenseUseCase, webContext);
        BalanceController balanceController = new BalanceController(getBalanceUseCase, webContext);

        AccountingRoutes.register(config, listPaymentsController, listPayrollController,
                listExpensesController, balanceController);
    }
}
