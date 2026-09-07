package com.library.accounting.application.service.payroll;

import com.library.accounting.application.dto.command.payroll.CreatePayrollCommand;
import com.library.accounting.domain.model.payroll.PayrollPayment;
import com.library.accounting.domain.port.out.PayrollRepository;
import com.library.kernel.generation.CodeGenerationService;

import java.math.BigDecimal;

public class CreatePayrollUseCase {

    private final PayrollRepository payrollRepository;
    private final CodeGenerationService codeGenerationService;

    public CreatePayrollUseCase(PayrollRepository payrollRepository,
                                CodeGenerationService codeGenerationService) {
        this.payrollRepository = payrollRepository;
        this.codeGenerationService = codeGenerationService;
    }

    public PayrollPayment execute(CreatePayrollCommand command, String processedBy) {
        if (command.amount() == null || command.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (command.paymentDate() == null) {
            throw new IllegalArgumentException("Payment date is required");
        }
        if (command.employeeId() == null) {
            throw new IllegalArgumentException("Employee is required");
        }

        String code = codeGenerationService.generate("PAYROLL");

        PayrollPayment payment = PayrollPayment.of(
                code,
                command.paymentDate(),
                command.employeeId(),
                command.amount(),
                command.periodMonth(),
                command.periodYear(),
                command.paymentMethod() != null ? command.paymentMethod() : "BANK_TRANSFER",
                command.notes(),
                processedBy
        );

        return payrollRepository.save(payment);
    }
}
