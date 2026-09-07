package com.library.accounting.infrastructure.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.envers.Audited;

import com.library.accounting.domain.model.expense.Expense;
import com.library.accounting.domain.model.expense.ExpenseCategory;
import com.library.iam.infrastructure.persistence.entity.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "expenses")
@Audited
public class ExpenseEntity extends AuditableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ExpenseCategory category;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_method", length = 30)
    private String paymentMethod;

    @Column(length = 200)
    private String vendor;

    @Column(name = "receipt_number", length = 100)
    private String receiptNumber;

    @Column(length = 500)
    private String notes;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    public ExpenseEntity() {}

    public static ExpenseEntity fromDomain(Expense e) {
        ExpenseEntity entity = new ExpenseEntity();
        entity.id = e.getId();
        entity.code = e.getCode();
        entity.expenseDate = e.getExpenseDate();
        entity.category = e.getCategory();
        entity.description = e.getDescription();
        entity.amount = e.getAmount();
        entity.paymentMethod = e.getPaymentMethod();
        entity.vendor = e.getVendor();
        entity.receiptNumber = e.getReceiptNumber();
        entity.notes = e.getNotes();
        entity.createdBy = e.getCreatedBy();
        entity.setCreatedAt(e.getCreatedAt());
        entity.setUpdatedAt(e.getUpdatedAt());
        return entity;
    }

    public Expense toDomain() {
        Expense e = new Expense(id, code, expenseDate, category, description, amount,
                paymentMethod, vendor, receiptNumber, notes, createdBy);
        e.setCreatedAt(getCreatedAt());
        e.setUpdatedAt(getUpdatedAt());
        return e;
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
}
