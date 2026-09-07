package com.library.stock.infrastructure.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.library.iam.infrastructure.persistence.entity.AuditableEntity;
import com.library.stock.domain.model.StockItemCondition;
import com.library.stock.domain.model.StockItemState;

@Entity
@Table(name = "stock_items")
@Audited
public class StockItemEntity extends AuditableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "edition_id", nullable = false)
    private Long editionId;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Column(name = "reservation_id")
    private Long reservationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StockItemState state = StockItemState.AVAILABLE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StockItemCondition condition = StockItemCondition.GOOD;

    @Column(name = "daily_price", precision = 10, scale = 2)
    private BigDecimal dailyPrice;

    @Column(nullable = false)
    private boolean enabled = true;

    public StockItemEntity() {
    }

    public StockItemEntity(Long id, String code, Long editionId, Long locationId,
                           Long reservationId, StockItemState state, StockItemCondition condition,
                           BigDecimal dailyPrice, boolean enabled) {
        this.id = id;
        this.code = code;
        this.editionId = editionId;
        this.locationId = locationId;
        this.reservationId = reservationId;
        this.state = state;
        this.condition = condition;
        this.dailyPrice = dailyPrice;
        this.enabled = enabled;
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
}
