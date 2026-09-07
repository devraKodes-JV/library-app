package com.library.accounting.application.service.account;

import com.library.accounting.domain.model.Account;
import com.library.accounting.domain.port.out.AccountRepository;

import java.util.List;

public class ListAccountsUseCase {

    private final AccountRepository accountRepository;

    public ListAccountsUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> execute() {
        return accountRepository.findAll();
    }
}
