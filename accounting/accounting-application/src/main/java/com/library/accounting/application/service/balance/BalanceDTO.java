package com.library.accounting.application.service.balance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record BalanceDTO(
        BigDecimal totalIncome,
        BigDecimal totalExpenses,
        BigDecimal totalPayroll,
        BigDecimal netBalance,
        List<IncomeByMethodRow> incomeByMethod,
        List<PaymentRow> recentPayments,
        List<ExpenseRow> recentExpenses
) {
    public record PaymentRow(Long id, String code, LocalDate date, String clientName, String method, BigDecimal amount) {}
    public record ExpenseRow(Long id, String code, LocalDate date, String category, String description, BigDecimal amount) {}
    public record IncomeByMethodRow(String method, BigDecimal amount, BigDecimal totalIncome) {}
}
