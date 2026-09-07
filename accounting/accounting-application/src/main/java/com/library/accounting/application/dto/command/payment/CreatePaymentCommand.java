package com.library.accounting.application.dto.command.payment;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreatePaymentCommand(
        LocalDate paymentDate,
        BigDecimal amount,
        String paymentMethod,
        String category,
        Long clientId,
        String referenceType,
        Long referenceId,
        String notes) {
}
