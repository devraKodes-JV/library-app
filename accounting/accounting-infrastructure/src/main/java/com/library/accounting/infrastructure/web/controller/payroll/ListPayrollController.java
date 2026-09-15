package com.library.accounting.infrastructure.web.controller.payroll;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.library.accounting.application.dto.command.payroll.CreatePayrollCommand;
import com.library.accounting.application.dto.response.payroll.PayrollResponseDTO;
import com.library.accounting.application.service.payroll.CreatePayrollUseCase;
import com.library.accounting.application.service.payroll.ListPayrollUseCase;
import com.library.accounting.application.service.paymentmethod.ListPaymentMethodsUseCase;
import com.library.accounting.domain.model.payroll.PayrollPayment;
import com.library.accounting.infrastructure.persistence.mapper.PayrollMapper;
import com.library.iam.application.dto.UserDTO;
import com.library.iam.application.service.user.ListActiveUsersUseCase;
import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class ListPayrollController extends BaseController {

    private final ListPayrollUseCase listPayrollUseCase;
    private final ListActiveUsersUseCase listActiveUsersUseCase;
    private final CreatePayrollUseCase createPayrollUseCase;
    private final ListPaymentMethodsUseCase listPaymentMethodsUseCase;

    public ListPayrollController(ListPayrollUseCase listPayrollUseCase,
                                 ListActiveUsersUseCase listActiveUsersUseCase,
                                 CreatePayrollUseCase createPayrollUseCase,
                                 ListPaymentMethodsUseCase listPaymentMethodsUseCase,
                                 WebControllerContext webContext) {
        super(webContext);
        this.listPayrollUseCase = listPayrollUseCase;
        this.listActiveUsersUseCase = listActiveUsersUseCase;
        this.createPayrollUseCase = createPayrollUseCase;
        this.listPaymentMethodsUseCase = listPaymentMethodsUseCase;
    }

    public void listPayroll(Context ctx) {
        requireCan(ctx, "accounting.payroll.read");
        List<PayrollPayment> payments = listPayrollUseCase.execute();
        List<PayrollResponseDTO> payrollDTOs = payments.stream()
                .map(PayrollMapper::toDTO)
                .toList();
        ctx.render("accounting/payroll/list", buildListModel(ctx, Map.of(
                "payrolls", payrollDTOs,
                "canCreate", hasPermission(ctx, "accounting.payroll.create"))));
    }

    public void showCreateForm(Context ctx) {
        requireCan(ctx, "accounting.payroll.create");
        List<UserDTO> employees = listActiveUsersUseCase.execute();
        ctx.render("accounting/payroll/form", buildListModel(ctx, Map.of(
                "employees", employees,
                "paymentMethods", listPaymentMethodsUseCase.execute())));
    }

    public void createPayroll(Context ctx) {
        requireCan(ctx, "accounting.payroll.create");
        CreatePayrollCommand command = new CreatePayrollCommand(
                parseDate(ctx.formParam("paymentDate")),
                parseLong(ctx.formParam("employeeId")),
                parseBigDecimal(ctx.formParam("amount")),
                parseInt(ctx.formParam("periodMonth")),
                parseInt(ctx.formParam("periodYear")),
                ctx.formParam("paymentMethod"),
                ctx.formParam("notes")
        );

        try {
            createPayrollUseCase.execute(command, currentUserName(ctx));
            flashSuccess(ctx, "Payroll payment recorded successfully.");
            ctx.redirect("/accounting/payroll");
        } catch (IllegalArgumentException e) {
            List<UserDTO> employees = listActiveUsersUseCase.execute();
            ctx.render("accounting/payroll/form", buildListModel(ctx, Map.of(
                    "employees", employees,
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
        if (value == null || value.isBlank()) return null;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseInt(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Integer.parseInt(value);
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
