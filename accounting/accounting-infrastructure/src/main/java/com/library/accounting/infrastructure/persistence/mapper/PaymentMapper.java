package com.library.accounting.infrastructure.persistence.mapper;

import com.library.accounting.application.dto.response.payment.PaymentResponseDTO;
import com.library.accounting.domain.model.Payment;

public class PaymentMapper {

    public static PaymentResponseDTO toDTO(Payment payment) {
        return new PaymentResponseDTO(
                payment.getId(),
                payment.getCode(),
                payment.getPaymentDate(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getCategory(),
                payment.getClientId(),
                payment.getClientName(),
                payment.getReferenceType(),
                payment.getReferenceId(),
                payment.getStatus(),
                payment.getReceivedBy(),
                payment.getNotes()
        );
    }
}
