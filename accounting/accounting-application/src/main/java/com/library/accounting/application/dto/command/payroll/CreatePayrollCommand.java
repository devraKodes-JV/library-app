package com.library.accounting.application.dto.command.payroll;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreatePayrollCommand(
        LocalDate paymentDate,
        Long employeeId,
        BigDecimal amount,
        Integer periodMonth,
        Integer periodYear,
        String paymentMethod,
        String notes) {
}
