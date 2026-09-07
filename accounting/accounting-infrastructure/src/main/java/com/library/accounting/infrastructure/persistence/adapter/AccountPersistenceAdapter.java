package com.library.accounting.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import com.library.accounting.domain.model.Account;
import com.library.accounting.domain.port.out.AccountRepository;
import com.library.accounting.infrastructure.persistence.repository.hibernate.HibernateAccountRepository;

public class AccountPersistenceAdapter implements AccountRepository {

    private final HibernateAccountRepository hibernateRepository;

    public AccountPersistenceAdapter(HibernateAccountRepository hibernateRepository) {
        this.hibernateRepository = hibernateRepository;
    }

    @Override
    public Account save(Account account) {
        return hibernateRepository.save(account);
    }

    @Override
    public Optional<Account> findById(Long id) {
        return hibernateRepository.findById(id);
    }

    @Override
    public Optional<Account> findByCode(String code) {
        return hibernateRepository.findByCode(code);
    }

    @Override
    public List<Account> findAll() {
        return hibernateRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        hibernateRepository.delete(id);
    }
}
