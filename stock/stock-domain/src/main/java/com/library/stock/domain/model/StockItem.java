package com.library.stock.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public class StockItem {

    private Long id;
    private String code;
    private Long editionId;
    private Long locationId;
    private Long reservationId;
    private StockItemState state;
    private StockItemCondition condition;
    private BigDecimal dailyPrice;
    private boolean enabled = true;
    private Instant createdAt;
    private Instant updatedAt;

    public StockItem(Long id, String code, Long editionId, Long locationId,
                     Long reservationId, StockItemState state, StockItemCondition condition,
                     BigDecimal dailyPrice, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.code = code;
        this.editionId = editionId;
        this.locationId = locationId;
        this.reservationId = reservationId;
        this.state = state;
        this.condition = condition;
        this.dailyPrice = dailyPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static StockItem withoutId(String code, Long editionId, Long locationId,
                                      StockItemState state, StockItemCondition condition,
                                      BigDecimal dailyPrice) {
        return new StockItem(null, code, editionId, locationId, null, state, condition, dailyPrice, null, null);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Long getEditionId() { return editionId; }
    public void setEditionId(Long editionId) { this.editionId = editionId; }

    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }

    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }

    public StockItemState getState() { return state; }
    public void setState(StockItemState state) { this.state = state; }

    public StockItemCondition getCondition() { return condition; }
    public void setCondition(StockItemCondition condition) { this.condition = condition; }

    public BigDecimal getDailyPrice() { return dailyPrice; }
    public void setDailyPrice(BigDecimal dailyPrice) { this.dailyPrice = dailyPrice; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
