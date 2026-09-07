package com.library.accounting.infrastructure.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.envers.Audited;

import com.library.accounting.domain.model.Refund;
import com.library.iam.infrastructure.persistence.entity.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "refunds")
@Audited
public class RefundEntity extends AuditableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "refund_date", nullable = false)
    private LocalDate refundDate;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(length = 500)
    private String reason;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "client_name", length = 200)
    private String clientName;

    @Column(name = "processed_by", length = 100)
    private String processedBy;

    @Column(nullable = false, length = 20)
    private String status = "PENDING";

    @Column(length = 500)
    private String notes;

    public RefundEntity() {}

    public static RefundEntity fromDomain(Refund r) {
        RefundEntity e = new RefundEntity();
        e.id = r.getId();
        e.code = r.getCode();
        e.refundDate = r.getRefundDate();
        e.amount = r.getAmount();
        e.reason = r.getReason();
        e.paymentId = r.getPaymentId();
        e.clientId = r.getClientId();
        e.clientName = r.getClientName();
        e.processedBy = r.getProcessedBy();
        e.status = r.getStatus();
        e.notes = r.getNotes();
        e.setCreatedAt(r.getCreatedAt());
        e.setUpdatedAt(r.getUpdatedAt());
        return e;
    }

    public Refund toDomain() {
        Refund r = new Refund(id, code, refundDate, amount, reason, paymentId, clientId,
                clientName, processedBy, status, notes);
        r.setCreatedAt(getCreatedAt());
        r.setUpdatedAt(getUpdatedAt());
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public LocalDate getRefundDate() { return refundDate; }
    public void setRefundDate(LocalDate refundDate) { this.refundDate = refundDate; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }
    public String getProcessedBy() { return processedBy; }
    public void setProcessedBy(String processedBy) { this.processedBy = processedBy; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
