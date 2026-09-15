package com.library.accounting.application.service.paymentmethod;

import com.library.accounting.domain.model.PaymentMethod;
import com.library.accounting.domain.port.out.PaymentMethodRepository;

import java.time.LocalDateTime;
import java.util.List;

public class ListPaymentMethodsUseCase {
    private final PaymentMethodRepository paymentMethodRepository;

    public ListPaymentMethodsUseCase(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public List<PaymentMethod> execute() {
        return paymentMethodRepository.findAll();
    }
}
