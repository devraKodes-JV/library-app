package com.library.reservation.infrastructure.persistence.mapper;

import com.library.reservation.domain.model.Reservation;
import com.library.reservation.domain.model.ReservationStatus;
import com.library.reservation.infrastructure.persistence.entity.ReservationEntity;

public final class ReservationMapper {

    private ReservationMapper() {
    }

    public static Reservation toDomain(ReservationEntity e) {
        if (e == null) {
            return null;
        }
        return new Reservation(
                e.getId(),
                e.getCode(),
                e.getClientId(),
                e.getEditionId(),
                e.getStockItemId(),
                e.getStatus(),
                e.getDepositPercentage(),
                e.getDepositAmount(),
                e.isDepositPaid(),
                e.getTotalAmount(),
                e.getTotalPaid(),
                e.getReservationDate(),
                e.getPickupDeadline(),
                e.getPickupDate(),
                e.getDueDate(),
                e.getReturnDate(),
                e.getLateFeePerDay(),
                e.getLateFeeTotal(),
                e.getRenewalCount(),
                e.getMaxRenewals(),
                e.getNotes(),
                e.isEnabled(),
                e.getCreatedAt(),
                e.getUpdatedAt());
    }

    public static ReservationEntity toEntity(Reservation r) {
        if (r == null) {
            return null;
        }
        return new ReservationEntity(
                r.getId(),
                r.getCode(),
                r.getClientId(),
                r.getEditionId(),
                r.getStockItemId(),
                r.getStatus(),
                r.getDepositPercentage(),
                r.getDepositAmount(),
                r.isDepositPaid(),
                r.getTotalAmount(),
                r.getTotalPaid(),
                r.getReservationDate(),
                r.getPickupDeadline(),
                r.getPickupDate(),
                r.getDueDate(),
                r.getReturnDate(),
                r.getLateFeePerDay(),
                r.getLateFeeTotal(),
                r.getRenewalCount(),
                r.getMaxRenewals(),
                r.getNotes(),
                r.isEnabled());
    }
}
