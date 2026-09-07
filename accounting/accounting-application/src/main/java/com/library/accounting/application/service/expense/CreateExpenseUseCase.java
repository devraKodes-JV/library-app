package com.library.accounting.application.service.expense;

import com.library.accounting.application.dto.command.expense.CreateExpenseCommand;
import com.library.accounting.domain.model.expense.Expense;
import com.library.accounting.domain.model.expense.ExpenseCategory;
import com.library.accounting.domain.port.out.ExpenseRepository;
import com.library.kernel.generation.CodeGenerationService;

import java.math.BigDecimal;

public class CreateExpenseUseCase {

    private final ExpenseRepository expenseRepository;
    private final CodeGenerationService codeGenerationService;

    public CreateExpenseUseCase(ExpenseRepository expenseRepository,
                                CodeGenerationService codeGenerationService) {
        this.expenseRepository = expenseRepository;
        this.codeGenerationService = codeGenerationService;
    }

    public Expense execute(CreateExpenseCommand command, String createdBy) {
        if (command.amount() == null || command.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (command.expenseDate() == null) {
            throw new IllegalArgumentException("Expense date is required");
        }
        if (command.category() == null || command.category().isBlank()) {
            throw new IllegalArgumentException("Category is required");
        }
        if (command.description() == null || command.description().isBlank()) {
            throw new IllegalArgumentException("Description is required");
        }

        String code = codeGenerationService.generate("EXP");
        ExpenseCategory category = ExpenseCategory.valueOf(command.category());

        Expense expense = Expense.of(
                code,
                command.expenseDate(),
                category,
                command.description(),
                command.amount(),
                command.paymentMethod() != null ? command.paymentMethod() : "CASH",
                command.vendor(),
                command.receiptNumber(),
                command.notes(),
                createdBy
        );

        return expenseRepository.save(expense);
    }
}
