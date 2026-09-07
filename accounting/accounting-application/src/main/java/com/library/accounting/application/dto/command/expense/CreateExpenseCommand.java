package com.library.accounting.application.dto.command.expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateExpenseCommand(
        LocalDate expenseDate,
        String category,
        String description,
        BigDecimal amount,
        String paymentMethod,
        String vendor,
        String receiptNumber,
        String notes) {
}
