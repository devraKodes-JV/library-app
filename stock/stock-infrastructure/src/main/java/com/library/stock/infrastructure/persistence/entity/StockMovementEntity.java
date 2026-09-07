package com.library.stock.infrastructure.persistence.entity;

import java.io.Serializable;

import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stock_movements")
@Audited
public class StockMovementEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private com.library.stock.domain.model.MovementType type;

    @Column(name = "stock_item_id", nullable = false)
    private Long stockItemId;

    @Column(name = "source_location_id")
    private Long sourceLocationId;

    @Column(name = "target_location_id")
    private Long targetLocationId;

    @Column(length = 500)
    private String reason;

    @Column(name = "created_at")
    private java.time.Instant createdAt;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    public StockMovementEntity() {
    }

    public StockMovementEntity(Long id, com.library.stock.domain.model.MovementType type,
                               Long stockItemId, Long sourceLocationId, Long targetLocationId,
                               String reason, java.time.Instant createdAt, String createdBy) {
        this.id = id;
        this.type = type;
        this.stockItemId = stockItemId;
        this.sourceLocationId = sourceLocationId;
        this.targetLocationId = targetLocationId;
        this.reason = reason;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public com.library.stock.domain.model.MovementType getType() { return type; }
    public void setType(com.library.stock.domain.model.MovementType type) { this.type = type; }
    public Long getStockItemId() { return stockItemId; }
    public void setStockItemId(Long stockItemId) { this.stockItemId = stockItemId; }
    public Long getSourceLocationId() { return sourceLocationId; }
    public void setSourceLocationId(Long sourceLocationId) { this.sourceLocationId = sourceLocationId; }
    public Long getTargetLocationId() { return targetLocationId; }
    public void setTargetLocationId(Long targetLocationId) { this.targetLocationId = targetLocationId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public java.time.Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.Instant createdAt) { this.createdAt = createdAt; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}
