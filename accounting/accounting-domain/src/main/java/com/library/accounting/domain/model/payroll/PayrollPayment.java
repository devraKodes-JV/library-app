package com.library.accounting.domain.model.payroll;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class PayrollPayment {
    private Long id;
    private String code;
    private LocalDate paymentDate;
    private Long employeeId;
    private String employeeName;
    private BigDecimal amount;
    private Integer periodMonth;
    private Integer periodYear;
    private String paymentMethod;
    private PayrollStatus status;
    private String notes;
    private String processedBy;
    private Instant createdAt;
    private Instant updatedAt;

    public PayrollPayment(Long id, String code, LocalDate paymentDate, Long employeeId,
                          String employeeName, BigDecimal amount, Integer periodMonth,
                          Integer periodYear, String paymentMethod, PayrollStatus status,
                          String notes, String processedBy) {
        this.id = id;
        this.code = code;
        this.paymentDate = paymentDate;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.amount = amount;
        this.periodMonth = periodMonth;
        this.periodYear = periodYear;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.notes = notes;
        this.processedBy = processedBy;
    }

    public static PayrollPayment of(String code, LocalDate paymentDate, Long employeeId,
                                    BigDecimal amount, Integer periodMonth, Integer periodYear,
                                    String paymentMethod, String notes, String processedBy) {
        return new PayrollPayment(null, code, paymentDate, employeeId, null, amount,
                periodMonth, periodYear, paymentMethod,
                PayrollStatus.PAID, notes, processedBy);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Integer getPeriodMonth() { return periodMonth; }
    public void setPeriodMonth(Integer periodMonth) { this.periodMonth = periodMonth; }
    public Integer getPeriodYear() { return periodYear; }
    public void setPeriodYear(Integer periodYear) { this.periodYear = periodYear; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public PayrollStatus getStatus() { return status; }
    public void setStatus(PayrollStatus status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getProcessedBy() { return processedBy; }
    public void setProcessedBy(String processedBy) { this.processedBy = processedBy; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
