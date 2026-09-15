package com.library.bootstrap.factory;

import org.hibernate.SessionFactory;

import com.library.accounting.application.service.account.ListAccountsUseCase;
import com.library.accounting.application.service.balance.GetBalanceUseCase;
import com.library.accounting.application.service.expense.CreateExpenseUseCase;
import com.library.accounting.application.service.expense.ListExpensesUseCase;
import com.library.accounting.application.service.payment.CreatePaymentUseCase;
import com.library.accounting.application.service.payment.ListPaymentsUseCase;
import com.library.accounting.application.service.payment.RecordClientPaymentUseCase;
import com.library.accounting.application.service.paymentmethod.CreatePaymentMethodUseCase;
import com.library.accounting.application.service.paymentmethod.DeletePaymentMethodUseCase;
import com.library.accounting.application.service.paymentmethod.ListPaymentMethodsUseCase;
import com.library.accounting.application.service.payroll.CreatePayrollUseCase;
import com.library.accounting.application.service.payroll.ListPayrollUseCase;
import com.library.accounting.domain.port.out.AccountRepository;
import com.library.accounting.domain.port.out.ExpenseRepository;
import com.library.accounting.domain.port.out.PaymentMethodRepository;
import com.library.accounting.domain.port.out.PaymentRepository;
import com.library.accounting.domain.port.out.PayrollRepository;
import com.library.accounting.domain.port.out.RefundRepository;
import com.library.accounting.infrastructure.persistence.adapter.AccountPersistenceAdapter;
import com.library.accounting.infrastructure.persistence.adapter.ExpensePersistenceAdapter;
import com.library.accounting.infrastructure.persistence.adapter.PaymentMethodPersistenceAdapter;
import com.library.accounting.infrastructure.persistence.adapter.PaymentPersistenceAdapter;
import com.library.accounting.infrastructure.persistence.adapter.PayrollPersistenceAdapter;
import com.library.accounting.infrastructure.persistence.adapter.RefundPersistenceAdapter;
import com.library.accounting.infrastructure.persistence.repository.hibernate.HibernateAccountRepository;
import com.library.accounting.infrastructure.persistence.repository.hibernate.HibernateExpenseRepository;
import com.library.accounting.infrastructure.persistence.repository.hibernate.HibernatePaymentMethodRepository;
import com.library.accounting.infrastructure.persistence.repository.hibernate.HibernatePaymentRepository;
import com.library.accounting.infrastructure.persistence.repository.hibernate.HibernatePayrollRepository;
import com.library.accounting.infrastructure.persistence.repository.hibernate.HibernateRefundRepository;
import com.library.accounting.infrastructure.web.AccountingRoutes;
import com.library.accounting.infrastructure.web.controller.balance.BalanceController;
import com.library.accounting.infrastructure.web.controller.expense.ListExpensesController;
import com.library.accounting.infrastructure.web.controller.payment.ListPaymentsController;
import com.library.accounting.infrastructure.web.controller.paymentmethod.PaymentMethodController;
import com.library.accounting.infrastructure.web.controller.payroll.ListPayrollController;
import com.library.bootstrap.generation.ShortUuidCodeGenerationService;
import com.library.client.application.service.client.ListClientsUseCase;
import com.library.client.domain.port.out.ClientRepository;
import com.library.iam.application.service.user.ListActiveUsersUseCase;
import com.library.kernel.generation.CodeGenerationService;
import com.library.kernel.web.WebControllerContext;
import com.library.reservation.domain.port.out.PaymentRecorder;
import com.library.reservation.infrastructure.payment.AccountingPaymentRecorder;

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

        PaymentMethodRepository paymentMethodRepository = new PaymentMethodPersistenceAdapter(
                new HibernatePaymentMethodRepository(sessionFactory));
        ListPaymentMethodsUseCase listPaymentMethodsUseCase = new ListPaymentMethodsUseCase(paymentMethodRepository);
        CreatePaymentMethodUseCase createPaymentMethodUseCase = new CreatePaymentMethodUseCase(
                paymentMethodRepository, codeGenerationService);
        DeletePaymentMethodUseCase deletePaymentMethodUseCase = new DeletePaymentMethodUseCase(paymentMethodRepository);

        PaymentMethodController paymentMethodController = new PaymentMethodController(
                listPaymentMethodsUseCase, createPaymentMethodUseCase, deletePaymentMethodUseCase, webContext);

        // Cross-module use cases
        ListClientsUseCase listClientsUseCase = new ListClientsUseCase(clientRepository);
        ListActiveUsersUseCase listActiveUsersUseCase = new ListActiveUsersUseCase(
                com.library.bootstrap.factory.IamFactory.userPort(sessionFactory));

        // Controllers
        ListPaymentsController listPaymentsController = new ListPaymentsController(
                listPaymentsUseCase, listClientsUseCase, createPaymentUseCase, listPaymentMethodsUseCase, webContext);
        ListPayrollController listPayrollController = new ListPayrollController(
                listPayrollUseCase, listActiveUsersUseCase, createPayrollUseCase, listPaymentMethodsUseCase, webContext);
        ListExpensesController listExpensesController = new ListExpensesController(
                listExpensesUseCase, createExpenseUseCase, listPaymentMethodsUseCase, webContext);
        BalanceController balanceController = new BalanceController(getBalanceUseCase, webContext);

        AccountingRoutes.register(config, listPaymentsController, listPayrollController,
                listExpensesController, balanceController, paymentMethodController);
    }

    public static PaymentRepository paymentRepository(SessionFactory sessionFactory) {
        return new PaymentPersistenceAdapter(new HibernatePaymentRepository(sessionFactory));
    }

    public static RecordClientPaymentUseCase recordClientPaymentUseCase(SessionFactory sessionFactory) {
        PaymentRepository paymentRepository = paymentRepository(sessionFactory);
        CodeGenerationService codeGenerationService = new ShortUuidCodeGenerationService();
        return new RecordClientPaymentUseCase(paymentRepository, codeGenerationService);
    }

    public static GetBalanceUseCase getBalanceUseCase(SessionFactory sessionFactory) {
        PaymentRepository paymentRepository = paymentRepository(sessionFactory);
        ExpenseRepository expenseRepository = new ExpensePersistenceAdapter(new HibernateExpenseRepository(sessionFactory));
        PayrollRepository payrollRepository = new PayrollPersistenceAdapter(new HibernatePayrollRepository(sessionFactory));
        return new GetBalanceUseCase(paymentRepository, expenseRepository, payrollRepository);
    }

    public static PaymentRecorder paymentRecorder(SessionFactory sessionFactory, CodeGenerationService codeGenerationService) {
        RecordClientPaymentUseCase recordClientPaymentUseCase = recordClientPaymentUseCase(sessionFactory);
        return new AccountingPaymentRecorder(recordClientPaymentUseCase);
    }
}
