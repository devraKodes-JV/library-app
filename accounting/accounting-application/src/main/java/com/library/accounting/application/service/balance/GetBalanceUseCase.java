package com.library.accounting.application.service.balance;

import com.library.accounting.domain.model.Payment;
import com.library.accounting.domain.model.expense.Expense;
import com.library.accounting.domain.model.payroll.PayrollPayment;
import com.library.accounting.domain.port.out.ExpenseRepository;
import com.library.accounting.domain.port.out.PaymentRepository;
import com.library.accounting.domain.port.out.PayrollRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class GetBalanceUseCase {

    private final PaymentRepository paymentRepository;
    private final ExpenseRepository expenseRepository;
    private final PayrollRepository payrollRepository;

    public GetBalanceUseCase(PaymentRepository paymentRepository,
                             ExpenseRepository expenseRepository,
                             PayrollRepository payrollRepository) {
        this.paymentRepository = paymentRepository;
        this.expenseRepository = expenseRepository;
        this.payrollRepository = payrollRepository;
    }

    public BalanceDTO execute(LocalDate from, LocalDate to, String paymentMethod) {
        List<Payment> allPayments = paymentRepository.findByDateRange(from, to);
        List<Expense> allExpenses = expenseRepository.findByDateRange(from, to);
        List<PayrollPayment> allPayroll = payrollRepository.findByDateRange(from, to);

        List<Payment> filteredPayments = allPayments.stream()
                .filter(p -> paymentMethod == null || paymentMethod.isBlank() || paymentMethod.equals(p.getPaymentMethod()))
                .toList();

        BigDecimal totalIncome = filteredPayments.stream()
                .map(Payment::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = allExpenses.stream()
                .map(Expense::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPayroll = allPayroll.stream()
                .map(PayrollPayment::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netBalance = totalIncome.subtract(totalExpenses).subtract(totalPayroll);

        List<BalanceDTO.IncomeByMethodRow> incomeByMethod = filteredPayments.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getPaymentMethod() != null ? p.getPaymentMethod() : "UNKNOWN",
                        Collectors.reducing(BigDecimal.ZERO, Payment::getAmount, BigDecimal::add)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByKey())
                .map(e -> new BalanceDTO.IncomeByMethodRow(e.getKey(), e.getValue(), totalIncome))
                .toList();

        List<BalanceDTO.PaymentRow> recentPayments = filteredPayments.stream()
                .sorted(Comparator.comparing(Payment::getPaymentDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(20)
                .map(p -> new BalanceDTO.PaymentRow(
                        p.getId(), p.getCode(), p.getPaymentDate(),
                        p.getClientName(), p.getPaymentMethod(), p.getAmount()
                ))
                .toList();

        List<BalanceDTO.ExpenseRow> recentExpenses = allExpenses.stream()
                .sorted(Comparator.comparing(Expense::getExpenseDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(20)
                .map(e -> new BalanceDTO.ExpenseRow(
                        e.getId(), e.getCode(), e.getExpenseDate(),
                        e.getCategory() != null ? e.getCategory().name() : "UNKNOWN",
                        e.getDescription(), e.getAmount()
                ))
                .toList();

        return new BalanceDTO(totalIncome, totalExpenses, totalPayroll, netBalance, incomeByMethod, recentPayments, recentExpenses);
    }
}
