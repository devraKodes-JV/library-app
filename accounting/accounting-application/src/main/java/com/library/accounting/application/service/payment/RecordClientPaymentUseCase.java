package com.library.accounting.application.service.payment;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.library.accounting.domain.model.Payment;
import com.library.accounting.domain.port.out.PaymentRepository;
import com.library.kernel.generation.CodeGenerationService;

public class RecordClientPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final CodeGenerationService codeGenerationService;

    public RecordClientPaymentUseCase(PaymentRepository paymentRepository,
                                      CodeGenerationService codeGenerationService) {
        this.paymentRepository = paymentRepository;
        this.codeGenerationService = codeGenerationService;
    }

    public Payment execute(BigDecimal amount, String paymentMethod, String category,
                            Long clientId, String referenceType, Long referenceId,
                            String notes, String receivedBy) {
        String code = codeGenerationService.generate("PAY");

        Payment payment = Payment.of(
                code,
                LocalDate.now(),
                amount,
                paymentMethod,
                category,
                clientId,
                referenceType,
                referenceId,
                receivedBy,
                notes
        );

        return paymentRepository.save(payment);
    }
}
