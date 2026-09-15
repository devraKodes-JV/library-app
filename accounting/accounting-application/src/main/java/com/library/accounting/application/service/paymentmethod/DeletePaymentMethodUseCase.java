package com.library.accounting.application.service.paymentmethod;

import com.library.accounting.domain.port.out.PaymentMethodRepository;

public class DeletePaymentMethodUseCase {
    private final PaymentMethodRepository paymentMethodRepository;

    public DeletePaymentMethodUseCase(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public void execute(Long id) {
        paymentMethodRepository.deleteById(id);
    }
}
