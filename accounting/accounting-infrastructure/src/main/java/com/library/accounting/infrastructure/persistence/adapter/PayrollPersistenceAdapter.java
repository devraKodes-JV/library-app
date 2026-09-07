package com.library.accounting.infrastructure.persistence.adapter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.library.accounting.domain.model.payroll.PayrollPayment;
import com.library.accounting.domain.port.out.PayrollRepository;
import com.library.accounting.infrastructure.persistence.repository.hibernate.HibernatePayrollRepository;

public class PayrollPersistenceAdapter implements PayrollRepository {

    private final HibernatePayrollRepository hibernateRepository;

    public PayrollPersistenceAdapter(HibernatePayrollRepository hibernateRepository) {
        this.hibernateRepository = hibernateRepository;
    }

    @Override
    public PayrollPayment save(PayrollPayment payment) {
        return hibernateRepository.save(payment);
    }

    @Override
    public Optional<PayrollPayment> findById(Long id) {
        return hibernateRepository.findById(id);
    }

    @Override
    public List<PayrollPayment> findAll() {
        return hibernateRepository.findAll();
    }

    @Override
    public List<PayrollPayment> findByEmployeeId(Long employeeId) {
        return hibernateRepository.findByEmployeeId(employeeId);
    }

    @Override
    public List<PayrollPayment> findByPeriod(Integer month, Integer year) {
        return hibernateRepository.findByPeriod(month, year);
    }

    @Override
    public List<PayrollPayment> findByDateRange(LocalDate from, LocalDate to) {
        return hibernateRepository.findByDateRange(from, to);
    }
}
