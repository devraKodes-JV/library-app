package com.library.accounting.application.service.paymentmethod;

import com.library.accounting.domain.model.PaymentMethod;
import com.library.accounting.domain.port.out.PaymentMethodRepository;

import java.time.LocalDateTime;

public class UpdatePaymentMethodUseCase {
    private final PaymentMethodRepository paymentMethodRepository;

    public UpdatePaymentMethodUseCase(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public PaymentMethod execute(Long id, String name, Boolean enabled, String updatedBy) {
        PaymentMethod pm = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PaymentMethod not found: " + id));
        if (name != null && !name.isBlank()) {
            pm.setName(name.trim());
        }
        if (enabled != null) {
            pm.setEnabled(enabled);
        }
        pm.setUpdatedAt(LocalDateTime.now());
        pm.setUpdatedBy(updatedBy);
        return paymentMethodRepository.save(pm);
    }
}
