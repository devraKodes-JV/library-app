package com.library.accounting.infrastructure.persistence.mapper;

import com.library.accounting.application.dto.response.expense.ExpenseResponseDTO;
import com.library.accounting.domain.model.expense.Expense;
import com.library.accounting.domain.model.expense.ExpenseCategory;

public class ExpenseMapper {

    public static ExpenseResponseDTO toDTO(Expense expense) {
        return new ExpenseResponseDTO(
                expense.getId(),
                expense.getCode(),
                expense.getExpenseDate(),
                expense.getAmount(),
                expense.getCategory() != null ? expense.getCategory().name() : null,
                expense.getDescription(),
                expense.getVendor(),
                expense.getPaymentMethod(),
                expense.getNotes()
        );
    }
}
