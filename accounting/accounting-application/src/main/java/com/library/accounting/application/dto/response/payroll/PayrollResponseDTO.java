package com.library.accounting.application.dto.response.payroll;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PayrollResponseDTO(
        Long id,
        String code,
        LocalDate paymentDate,
        BigDecimal amount,
        String paymentMethod,
        String status,
        String employeeName,
        Long employeeId,
        Integer periodMonth,
        Integer periodYear,
        String notes
) {}
