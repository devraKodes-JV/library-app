package com.library.accounting.infrastructure.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.envers.Audited;

import com.library.accounting.domain.model.Payment;
import com.library.iam.infrastructure.persistence.entity.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "payments")
@Audited
public class PaymentEntity extends AuditableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_method", nullable = false, length = 30)
    private String paymentMethod;

    @Column(nullable = false, length = 30)
    private String category;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "client_name", length = 200)
    private String clientName;

    @Column(name = "reference_type", length = 50)
    private String referenceType;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(nullable = false, length = 20)
    private String status = "COMPLETED";

    @Column(name = "received_by", length = 100)
    private String receivedBy;

    @Column(length = 500)
    private String notes;

    public PaymentEntity() {}

    public static PaymentEntity fromDomain(Payment p) {
        PaymentEntity e = new PaymentEntity();
        e.id = p.getId();
        e.code = p.getCode();
        e.paymentDate = p.getPaymentDate();
        e.amount = p.getAmount();
        e.paymentMethod = p.getPaymentMethod();
        e.category = p.getCategory();
        e.clientId = p.getClientId();
        e.clientName = p.getClientName();
        e.referenceType = p.getReferenceType();
        e.referenceId = p.getReferenceId();
        e.status = p.getStatus();
        e.receivedBy = p.getReceivedBy();
        e.notes = p.getNotes();
        e.setCreatedAt(p.getCreatedAt());
        e.setUpdatedAt(p.getUpdatedAt());
        return e;
    }

    public Payment toDomain() {
        Payment p = new Payment(id, code, paymentDate, amount, paymentMethod, category,
                clientId, clientName, referenceType, referenceId, status, receivedBy, notes);
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
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }
    public String getReferenceType() { return referenceType; }
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }
    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReceivedBy() { return receivedBy; }
    public void setReceivedBy(String receivedBy) { this.receivedBy = receivedBy; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
