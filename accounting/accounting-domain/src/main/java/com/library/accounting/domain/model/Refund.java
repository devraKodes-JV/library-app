package com.library.accounting.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class Refund {
    private Long id;
    private String code;
    private LocalDate refundDate;
    private BigDecimal amount;
    private String reason;
    private Long paymentId;
    private Long clientId;
    private String clientName;
    private String processedBy;
    private String status;
    private String notes;
    private Instant createdAt;
    private Instant updatedAt;

    public Refund(Long id, String code, LocalDate refundDate, BigDecimal amount, String reason,
                 Long paymentId, Long clientId, String clientName, String processedBy,
                 String status, String notes) {
        this.id = id;
        this.code = code;
        this.refundDate = refundDate;
        this.amount = amount;
        this.reason = reason;
        this.paymentId = paymentId;
        this.clientId = clientId;
        this.clientName = clientName;
        this.processedBy = processedBy;
        this.status = status;
        this.notes = notes;
    }

    public static Refund of(String code, LocalDate refundDate, BigDecimal amount, String reason,
                            Long paymentId, Long clientId, String processedBy, String notes) {
        return new Refund(null, code, refundDate, amount, reason, paymentId, clientId, null,
                processedBy, "PENDING", notes);
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
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
