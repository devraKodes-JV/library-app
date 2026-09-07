package com.library.accounting.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class Payment {
    private Long id;
    private String code;
    private LocalDate paymentDate;
    private BigDecimal amount;
    private String paymentMethod;
    private String category;
    private Long clientId;
    private String clientName;
    private String referenceType;
    private Long referenceId;
    private String status;
    private String receivedBy;
    private String notes;
    private Instant createdAt;
    private Instant updatedAt;

    public Payment(Long id, String code, LocalDate paymentDate, BigDecimal amount,
                   String paymentMethod, String category, Long clientId, String clientName,
                   String referenceType, Long referenceId, String status,
                   String receivedBy, String notes) {
        this.id = id;
        this.code = code;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.category = category;
        this.clientId = clientId;
        this.clientName = clientName;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.status = status;
        this.receivedBy = receivedBy;
        this.notes = notes;
    }

    public static Payment of(String code, LocalDate paymentDate, BigDecimal amount,
                             String paymentMethod, String category, Long clientId,
                             String referenceType, Long referenceId,
                             String receivedBy, String notes) {
        return new Payment(null, code, paymentDate, amount, paymentMethod, category,
                clientId, null, referenceType, referenceId, "COMPLETED",
                receivedBy, notes);
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
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
