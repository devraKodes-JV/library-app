package com.library.accounting.application.service.payment;

import com.library.accounting.application.dto.command.payment.CreatePaymentCommand;
import com.library.accounting.domain.model.Payment;
import com.library.accounting.domain.port.out.PaymentRepository;
import com.library.kernel.generation.CodeGenerationService;

import java.math.BigDecimal;

public class CreatePaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final CodeGenerationService codeGenerationService;

    public CreatePaymentUseCase(PaymentRepository paymentRepository,
                                CodeGenerationService codeGenerationService) {
        this.paymentRepository = paymentRepository;
        this.codeGenerationService = codeGenerationService;
    }

    public Payment execute(CreatePaymentCommand command, String receivedBy) {
        if (command.amount() == null || command.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (command.paymentDate() == null) {
            throw new IllegalArgumentException("Payment date is required");
        }
        if (command.paymentMethod() == null || command.paymentMethod().isBlank()) {
            throw new IllegalArgumentException("Payment method is required");
        }

        String code = codeGenerationService.generate("PAY");
        while (paymentRepository.findById(code.hashCode() * 1L) != null
                && paymentRepository.findById(code.hashCode() * 1L).isPresent()) {
            code = codeGenerationService.generate("PAY");
        }

        Payment payment = Payment.of(
                code,
                command.paymentDate(),
                command.amount(),
                command.paymentMethod(),
                command.category() != null ? command.category() : "OTHER",
                command.clientId(),
                command.referenceType(),
                command.referenceId(),
                receivedBy,
                command.notes()
        );

        return paymentRepository.save(payment);
    }
}
