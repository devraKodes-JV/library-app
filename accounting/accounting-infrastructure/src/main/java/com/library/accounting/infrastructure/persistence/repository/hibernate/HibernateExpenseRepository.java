package com.library.accounting.infrastructure.persistence.repository.hibernate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.accounting.domain.model.expense.Expense;
import com.library.accounting.infrastructure.persistence.entity.ExpenseEntity;

public class HibernateExpenseRepository {

    private final SessionFactory sessionFactory;

    public HibernateExpenseRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Expense save(Expense expense) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            ExpenseEntity entity = ExpenseEntity.fromDomain(expense);
            if (entity.getId() == null) {
                session.persist(entity);
            } else {
                entity = session.merge(entity);
            }
            session.getTransaction().commit();
            return entity.toDomain();
        }
    }

    public Optional<Expense> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            ExpenseEntity e = session.get(ExpenseEntity.class, id);
            return Optional.ofNullable(e).map(ExpenseEntity::toDomain);
        }
    }

    public List<Expense> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from ExpenseEntity where deletedAt is null order by expenseDate desc, id desc", ExpenseEntity.class)
                    .getResultList().stream().map(ExpenseEntity::toDomain).toList();
        }
    }

    public List<Expense> findByDateRange(LocalDate from, LocalDate to) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from ExpenseEntity where deletedAt is null and expenseDate between :from and :to order by expenseDate desc", ExpenseEntity.class)
                    .setParameter("from", from)
                    .setParameter("to", to)
                    .getResultList().stream().map(ExpenseEntity::toDomain).toList();
        }
    }

    public List<Expense> findByCategory(String category) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from ExpenseEntity where deletedAt is null and category = :category order by expenseDate desc", ExpenseEntity.class)
                    .setParameter("category", category)
                    .getResultList().stream().map(ExpenseEntity::toDomain).toList();
        }
    }
}
