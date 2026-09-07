package com.library.reservation.infrastructure.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import org.hibernate.envers.Audited;

import com.library.iam.infrastructure.persistence.entity.AuditableEntity;
import com.library.reservation.domain.model.ReservationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservations")
@Audited
public class ReservationEntity extends AuditableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "edition_id", nullable = false)
    private Long editionId;

    @Column(name = "stock_item_id")
    private Long stockItemId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status;

    @Column(name = "deposit_percentage", nullable = false)
    private Integer depositPercentage;

    @Column(name = "deposit_amount", precision = 10, scale = 2)
    private BigDecimal depositAmount;

    @Column(name = "deposit_paid", nullable = false)
    private boolean depositPaid;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "total_paid", precision = 10, scale = 2)
    private BigDecimal totalPaid;

    @Column(name = "reservation_date")
    private Instant reservationDate;

    @Column(name = "pickup_deadline")
    private LocalDate pickupDeadline;

    @Column(name = "pickup_date")
    private Instant pickupDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "return_date")
    private Instant returnDate;

    @Column(name = "late_fee_per_day", precision = 10, scale = 2)
    private BigDecimal lateFeePerDay;

    @Column(name = "late_fee_total", precision = 10, scale = 2)
    private BigDecimal lateFeeTotal;

    @Column(name = "renewal_count")
    private Integer renewalCount;

    @Column(name = "max_renewals")
    private Integer maxRenewals;

    @Column(length = 500)
    private String notes;

    private boolean enabled = true;

    public ReservationEntity() {
    }

    public ReservationEntity(Long id, String code, Long clientId, Long editionId, Long stockItemId,
                             ReservationStatus status, Integer depositPercentage, BigDecimal depositAmount,
                             boolean depositPaid, BigDecimal totalAmount, BigDecimal totalPaid,
                             Instant reservationDate, LocalDate pickupDeadline, Instant pickupDate,
                             LocalDate dueDate, Instant returnDate, BigDecimal lateFeePerDay,
                             BigDecimal lateFeeTotal, Integer renewalCount, Integer maxRenewals,
                             String notes, boolean enabled) {
        this.id = id;
        this.code = code;
        this.clientId = clientId;
        this.editionId = editionId;
        this.stockItemId = stockItemId;
        this.status = status;
        this.depositPercentage = depositPercentage;
        this.depositAmount = depositAmount;
        this.depositPaid = depositPaid;
        this.totalAmount = totalAmount;
        this.totalPaid = totalPaid;
        this.reservationDate = reservationDate;
        this.pickupDeadline = pickupDeadline;
        this.pickupDate = pickupDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.lateFeePerDay = lateFeePerDay;
        this.lateFeeTotal = lateFeeTotal;
        this.renewalCount = renewalCount;
        this.maxRenewals = maxRenewals;
        this.notes = notes;
        this.enabled = enabled;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public Long getEditionId() { return editionId; }
    public void setEditionId(Long editionId) { this.editionId = editionId; }

    public Long getStockItemId() { return stockItemId; }
    public void setStockItemId(Long stockItemId) { this.stockItemId = stockItemId; }

    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }

    public Integer getDepositPercentage() { return depositPercentage; }
    public void setDepositPercentage(Integer depositPercentage) { this.depositPercentage = depositPercentage; }

    public BigDecimal getDepositAmount() { return depositAmount; }
    public void setDepositAmount(BigDecimal depositAmount) { this.depositAmount = depositAmount; }

    public boolean isDepositPaid() { return depositPaid; }
    public void setDepositPaid(boolean depositPaid) { this.depositPaid = depositPaid; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BigDecimal getTotalPaid() { return totalPaid; }
    public void setTotalPaid(BigDecimal totalPaid) { this.totalPaid = totalPaid; }

    public Instant getReservationDate() { return reservationDate; }
    public void setReservationDate(Instant reservationDate) { this.reservationDate = reservationDate; }

    public LocalDate getPickupDeadline() { return pickupDeadline; }
    public void setPickupDeadline(LocalDate pickupDeadline) { this.pickupDeadline = pickupDeadline; }

    public Instant getPickupDate() { return pickupDate; }
    public void setPickupDate(Instant pickupDate) { this.pickupDate = pickupDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Instant getReturnDate() { return returnDate; }
    public void setReturnDate(Instant returnDate) { this.returnDate = returnDate; }

    public BigDecimal getLateFeePerDay() { return lateFeePerDay; }
    public void setLateFeePerDay(BigDecimal lateFeePerDay) { this.lateFeePerDay = lateFeePerDay; }

    public BigDecimal getLateFeeTotal() { return lateFeeTotal; }
    public void setLateFeeTotal(BigDecimal lateFeeTotal) { this.lateFeeTotal = lateFeeTotal; }

    public Integer getRenewalCount() { return renewalCount; }
    public void setRenewalCount(Integer renewalCount) { this.renewalCount = renewalCount; }

    public Integer getMaxRenewals() { return maxRenewals; }
    public void setMaxRenewals(Integer maxRenewals) { this.maxRenewals = maxRenewals; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
