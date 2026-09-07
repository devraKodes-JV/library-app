package com.library.reservation.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class Reservation {

    private Long id;
    private String code;
    private Long clientId;
    private Long editionId;
    private Long stockItemId;
    private ReservationStatus status;
    private Integer depositPercentage;
    private BigDecimal depositAmount;
    private boolean depositPaid;
    private BigDecimal totalAmount;
    private BigDecimal totalPaid;
    private Instant reservationDate;
    private LocalDate pickupDeadline;
    private Instant pickupDate;
    private LocalDate dueDate;
    private Instant returnDate;
    private BigDecimal lateFeePerDay;
    private BigDecimal lateFeeTotal;
    private Integer renewalCount;
    private Integer maxRenewals;
    private String notes;
    private boolean enabled = true;
    private Instant createdAt;
    private Instant updatedAt;

    public Reservation(Long id, String code, Long clientId, Long editionId, Long stockItemId,
                       ReservationStatus status, Integer depositPercentage, BigDecimal depositAmount,
                       boolean depositPaid, BigDecimal totalAmount, BigDecimal totalPaid,
                       Instant reservationDate, LocalDate pickupDeadline, Instant pickupDate,
                       LocalDate dueDate, Instant returnDate, BigDecimal lateFeePerDay,
                       BigDecimal lateFeeTotal, Integer renewalCount, Integer maxRenewals,
                       String notes, boolean enabled, Instant createdAt, Instant updatedAt) {
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
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Reservation withoutId(String code, Long clientId, Long editionId, Long stockItemId,
                                        ReservationStatus status, Integer depositPercentage,
                                        BigDecimal depositAmount, BigDecimal totalAmount,
                                        LocalDate pickupDeadline, LocalDate dueDate,
                                        BigDecimal lateFeePerDay, Integer maxRenewals) {
        return new Reservation(null, code, clientId, editionId, stockItemId, status,
                depositPercentage, depositAmount, false, totalAmount, BigDecimal.ZERO,
                null, pickupDeadline, null, dueDate, null, lateFeePerDay,
                BigDecimal.ZERO, 0, maxRenewals, null, true, null, null);
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

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
