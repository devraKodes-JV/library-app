package com.library.accounting.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import com.library.accounting.domain.model.Refund;
import com.library.accounting.domain.port.out.RefundRepository;
import com.library.accounting.infrastructure.persistence.repository.hibernate.HibernateRefundRepository;

public class RefundPersistenceAdapter implements RefundRepository {

    private final HibernateRefundRepository hibernateRepository;

    public RefundPersistenceAdapter(HibernateRefundRepository hibernateRepository) {
        this.hibernateRepository = hibernateRepository;
    }

    @Override
    public Refund save(Refund refund) {
        return hibernateRepository.save(refund);
    }

    @Override
    public Optional<Refund> findById(Long id) {
        return hibernateRepository.findById(id);
    }

    @Override
    public List<Refund> findAll() {
        return hibernateRepository.findAll();
    }
}
