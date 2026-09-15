package com.library.accounting.infrastructure.persistence.adapter;

import com.library.accounting.domain.port.out.PaymentMethodRepository;
import com.library.accounting.infrastructure.persistence.repository.hibernate.HibernatePaymentMethodRepository;

public class PaymentMethodPersistenceAdapter implements PaymentMethodRepository {
    private final HibernatePaymentMethodRepository hibernateRepository;

    public PaymentMethodPersistenceAdapter(HibernatePaymentMethodRepository hibernateRepository) {
        this.hibernateRepository = hibernateRepository;
    }

    @Override
    public java.util.List<com.library.accounting.domain.model.PaymentMethod> findAll() {
        return hibernateRepository.findAll();
    }

    @Override
    public java.util.List<com.library.accounting.domain.model.PaymentMethod> findAllEnabled() {
        return hibernateRepository.findAllEnabled();
    }

    @Override
    public java.util.Optional<com.library.accounting.domain.model.PaymentMethod> findById(Long id) {
        return hibernateRepository.findById(id);
    }

    @Override
    public java.util.Optional<com.library.accounting.domain.model.PaymentMethod> findByCode(String code) {
        return hibernateRepository.findByCode(code);
    }

    @Override
    public com.library.accounting.domain.model.PaymentMethod save(com.library.accounting.domain.model.PaymentMethod paymentMethod) {
        return hibernateRepository.save(paymentMethod);
    }

    @Override
    public void deleteById(Long id) {
        hibernateRepository.deleteById(id);
    }
}
