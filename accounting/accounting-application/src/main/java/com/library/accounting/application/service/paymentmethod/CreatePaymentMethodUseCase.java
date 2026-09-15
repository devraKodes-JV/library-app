package com.library.accounting.application.service.paymentmethod;

import com.library.accounting.domain.model.PaymentMethod;
import com.library.accounting.domain.port.out.PaymentMethodRepository;
import com.library.kernel.generation.CodeGenerationService;

import java.time.LocalDateTime;

public class CreatePaymentMethodUseCase {
    private final PaymentMethodRepository paymentMethodRepository;
    private final CodeGenerationService codeGenerationService;

    public CreatePaymentMethodUseCase(PaymentMethodRepository paymentMethodRepository,
                                      CodeGenerationService codeGenerationService) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.codeGenerationService = codeGenerationService;
    }

    public PaymentMethod execute(String name, String createdBy) {
        PaymentMethod pm = new PaymentMethod();
        pm.setCode(codeGenerationService.generate("PMT"));
        pm.setName(name.trim());
        pm.setEnabled(true);
        pm.setCreatedAt(LocalDateTime.now());
        pm.setUpdatedAt(LocalDateTime.now());
        pm.setCreatedBy(createdBy);
        pm.setUpdatedBy(createdBy);
        return paymentMethodRepository.save(pm);
    }
}
