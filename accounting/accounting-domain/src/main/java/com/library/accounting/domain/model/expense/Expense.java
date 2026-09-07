package com.library.accounting.domain.model.expense;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class Expense {
    private Long id;
    private String code;
    private LocalDate expenseDate;
    private ExpenseCategory category;
    private String description;
    private BigDecimal amount;
    private String paymentMethod;
    private String vendor;
    private String receiptNumber;
    private String notes;
    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;

    public Expense(Long id, String code, LocalDate expenseDate, ExpenseCategory category,
                   String description, BigDecimal amount, String paymentMethod,
                   String vendor, String receiptNumber, String notes, String createdBy) {
        this.id = id;
        this.code = code;
        this.expenseDate = expenseDate;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.vendor = vendor;
        this.receiptNumber = receiptNumber;
        this.notes = notes;
        this.createdBy = createdBy;
    }

    public static Expense of(String code, LocalDate expenseDate, ExpenseCategory category,
                             String description, BigDecimal amount, String paymentMethod,
                             String vendor, String receiptNumber, String notes, String createdBy) {
        return new Expense(null, code, expenseDate, category, description, amount,
                paymentMethod, vendor, receiptNumber, notes, createdBy);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public LocalDate getExpenseDate() { return expenseDate; }
    public void setExpenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; }
    public ExpenseCategory getCategory() { return category; }
    public void setCategory(ExpenseCategory category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getVendor() { return vendor; }
    public void setVendor(String vendor) { this.vendor = vendor; }
    public String getReceiptNumber() { return receiptNumber; }
    public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
