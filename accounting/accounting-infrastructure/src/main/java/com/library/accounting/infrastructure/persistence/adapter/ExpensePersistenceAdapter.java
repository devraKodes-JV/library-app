package com.library.accounting.infrastructure.persistence.adapter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.library.accounting.domain.model.expense.Expense;
import com.library.accounting.domain.port.out.ExpenseRepository;
import com.library.accounting.infrastructure.persistence.repository.hibernate.HibernateExpenseRepository;

public class ExpensePersistenceAdapter implements ExpenseRepository {

    private final HibernateExpenseRepository hibernateRepository;

    public ExpensePersistenceAdapter(HibernateExpenseRepository hibernateRepository) {
        this.hibernateRepository = hibernateRepository;
    }

    @Override
    public Expense save(Expense expense) {
        return hibernateRepository.save(expense);
    }

    @Override
    public Optional<Expense> findById(Long id) {
        return hibernateRepository.findById(id);
    }

    @Override
    public List<Expense> findAll() {
        return hibernateRepository.findAll();
    }

    @Override
    public List<Expense> findByDateRange(LocalDate from, LocalDate to) {
        return hibernateRepository.findByDateRange(from, to);
    }

    @Override
    public List<Expense> findByCategory(String category) {
        return hibernateRepository.findByCategory(category);
    }
}
