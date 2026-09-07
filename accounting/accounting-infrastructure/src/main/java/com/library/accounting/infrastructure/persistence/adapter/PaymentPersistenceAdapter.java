package com.library.accounting.infrastructure.persistence.adapter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.library.accounting.domain.model.Payment;
import com.library.accounting.domain.port.out.PaymentRepository;
import com.library.accounting.infrastructure.persistence.repository.hibernate.HibernatePaymentRepository;

public class PaymentPersistenceAdapter implements PaymentRepository {

    private final HibernatePaymentRepository hibernateRepository;

    public PaymentPersistenceAdapter(HibernatePaymentRepository hibernateRepository) {
        this.hibernateRepository = hibernateRepository;
    }

    @Override
    public Payment save(Payment payment) {
        return hibernateRepository.save(payment);
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return hibernateRepository.findById(id);
    }

    @Override
    public List<Payment> findAll() {
        return hibernateRepository.findAll();
    }

    @Override
    public List<Payment> findByDateRange(LocalDate from, LocalDate to) {
        return hibernateRepository.findByDateRange(from, to);
    }

    @Override
    public List<Payment> findByClientId(Long clientId) {
        return hibernateRepository.findByClientId(clientId);
    }
}
