package com.library.accounting.infrastructure.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.envers.Audited;

import com.library.accounting.domain.model.payroll.PayrollPayment;
import com.library.accounting.domain.model.payroll.PayrollStatus;
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
@Table(name = "payroll_payments")
@Audited
public class PayrollPaymentEntity extends AuditableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "employee_name", length = 200)
    private String employeeName;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "period_month")
    private Integer periodMonth;

    @Column(name = "period_year")
    private Integer periodYear;

    @Column(name = "payment_method", length = 30)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PayrollStatus status = PayrollStatus.PAID;

    @Column(length = 500)
    private String notes;

    @Column(name = "processed_by", length = 100)
    private String processedBy;

    public PayrollPaymentEntity() {}

    public static PayrollPaymentEntity fromDomain(PayrollPayment p) {
        PayrollPaymentEntity e = new PayrollPaymentEntity();
        e.id = p.getId();
        e.code = p.getCode();
        e.paymentDate = p.getPaymentDate();
        e.employeeId = p.getEmployeeId();
        e.employeeName = p.getEmployeeName();
        e.amount = p.getAmount();
        e.periodMonth = p.getPeriodMonth();
        e.periodYear = p.getPeriodYear();
        e.paymentMethod = p.getPaymentMethod();
        e.status = p.getStatus();
        e.notes = p.getNotes();
        e.processedBy = p.getProcessedBy();
        e.setCreatedAt(p.getCreatedAt());
        e.setUpdatedAt(p.getUpdatedAt());
        return e;
    }

    public PayrollPayment toDomain() {
        PayrollPayment p = new PayrollPayment(id, code, paymentDate, employeeId, employeeName,
                amount, periodMonth, periodYear, paymentMethod, status, notes, processedBy);
        p.setCreatedAt(getCreatedAt());
        p.setUpdatedAt(getUpdatedAt());
        return p;
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
}
