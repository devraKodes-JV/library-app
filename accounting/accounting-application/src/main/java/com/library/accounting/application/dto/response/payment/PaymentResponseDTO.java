package com.library.accounting.application.dto.response.payment;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentResponseDTO(
        Long id,
        String code,
        LocalDate paymentDate,
        BigDecimal amount,
        String paymentMethod,
        String category,
        Long clientId,
        String clientName,
        String referenceType,
        Long referenceId,
        String status,
        String receivedBy,
        String notes
) {}
