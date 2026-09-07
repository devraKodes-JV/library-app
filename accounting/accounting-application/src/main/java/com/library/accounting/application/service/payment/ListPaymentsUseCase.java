package com.library.accounting.application.service.payment;

import com.library.accounting.domain.model.Payment;
import com.library.accounting.domain.port.out.PaymentRepository;

import java.time.LocalDate;
import java.util.List;

public class ListPaymentsUseCase {

    private final PaymentRepository paymentRepository;

    public ListPaymentsUseCase(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> execute() {
        return paymentRepository.findAll();
    }

    public List<Payment> execute(LocalDate from, LocalDate to) {
        return paymentRepository.findByDateRange(from, to);
    }
}
