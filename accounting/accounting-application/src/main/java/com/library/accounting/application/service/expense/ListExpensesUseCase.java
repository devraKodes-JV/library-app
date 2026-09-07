package com.library.accounting.application.service.expense;

import com.library.accounting.domain.model.expense.Expense;
import com.library.accounting.domain.port.out.ExpenseRepository;

import java.util.List;

public class ListExpensesUseCase {

    private final ExpenseRepository expenseRepository;

    public ListExpensesUseCase(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> execute() {
        return expenseRepository.findAll();
    }
}
