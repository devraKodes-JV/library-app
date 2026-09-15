package com.library.accounting.application.dto.response.expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResponseDTO(
        Long id,
        String code,
        LocalDate expenseDate,
        BigDecimal amount,
        String category,
        String description,
        String vendor,
        String paymentMethod,
        String notes
) {}
