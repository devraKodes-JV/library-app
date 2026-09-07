package com.library.stock.domain.model;

import java.time.Instant;

public class StockMovement {

    private Long id;
    private MovementType type;
    private Long stockItemId;
    private Long sourceLocationId;
    private Long targetLocationId;
    private String reason;
    private Instant createdAt;
    private String createdBy;

    public StockMovement(Long id, MovementType type, Long stockItemId, Long sourceLocationId,
                         Long targetLocationId, String reason, Instant createdAt, String createdBy) {
        this.id = id;
        this.type = type;
        this.stockItemId = stockItemId;
        this.sourceLocationId = sourceLocationId;
        this.targetLocationId = targetLocationId;
        this.reason = reason;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public static StockMovement withoutId(MovementType type, Long stockItemId, Long sourceLocationId,
                                          Long targetLocationId, String reason) {
        return new StockMovement(null, type, stockItemId, sourceLocationId, targetLocationId, reason, null, null);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MovementType getType() { return type; }
    public void setType(MovementType type) { this.type = type; }

    public Long getStockItemId() { return stockItemId; }
    public void setStockItemId(Long stockItemId) { this.stockItemId = stockItemId; }

    public Long getSourceLocationId() { return sourceLocationId; }
    public void setSourceLocationId(Long sourceLocationId) { this.sourceLocationId = sourceLocationId; }

    public Long getTargetLocationId() { return targetLocationId; }
    public void setTargetLocationId(Long targetLocationId) { this.targetLocationId = targetLocationId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}
