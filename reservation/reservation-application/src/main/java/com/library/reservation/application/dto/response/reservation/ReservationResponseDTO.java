package com.library.reservation.application.dto.response.reservation;

import com.library.reservation.domain.model.Reservation;
import com.library.reservation.domain.model.ReservationStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record ReservationResponseDTO(
        Long id,
        String code,
        Long clientId,
        String clientName,
        String clientDni,
        boolean clientDataIncomplete,
        Long editionId,
        String editionName,
        Long stockItemId,
        ReservationStatus status,
        String statusLabel,
        Integer depositPercentage,
        BigDecimal depositAmount,
        boolean depositPaid,
        BigDecimal totalAmount,
        BigDecimal totalPaid,
        Instant reservationDate,
        LocalDate pickupDeadline,
        Instant pickupDate,
        LocalDate dueDate,
        Instant returnDate,
        BigDecimal lateFeePerDay,
        BigDecimal lateFeeTotal,
        Integer renewalCount,
        Integer maxRenewals,
        String notes,
        Instant createdAt,
        Instant updatedAt) {

    public static ReservationResponseDTO of(Reservation reservation) {
        return new ReservationResponseDTO(
                reservation.getId(),
                reservation.getCode(),
                reservation.getClientId(),
                null,
                null,
                false,
                reservation.getEditionId(),
                null,
                reservation.getStockItemId(),
                reservation.getStatus(),
                reservation.getStatus() != null ? reservation.getStatus().getLabel() : null,
                reservation.getDepositPercentage(),
                reservation.getDepositAmount(),
                reservation.isDepositPaid(),
                reservation.getTotalAmount(),
                reservation.getTotalPaid(),
                reservation.getReservationDate(),
                reservation.getPickupDeadline(),
                reservation.getPickupDate(),
                reservation.getDueDate(),
                reservation.getReturnDate(),
                reservation.getLateFeePerDay(),
                reservation.getLateFeeTotal(),
                reservation.getRenewalCount(),
                reservation.getMaxRenewals(),
                reservation.getNotes(),
                reservation.getCreatedAt(),
                reservation.getUpdatedAt());
    }

    public static ReservationResponseDTO of(Reservation reservation, String clientName, String clientDni, boolean clientDataIncomplete, String editionName) {
        return new ReservationResponseDTO(
                reservation.getId(),
                reservation.getCode(),
                reservation.getClientId(),
                clientName,
                clientDni,
                clientDataIncomplete,
                reservation.getEditionId(),
                editionName,
                reservation.getStockItemId(),
                reservation.getStatus(),
                reservation.getStatus() != null ? reservation.getStatus().getLabel() : null,
                reservation.getDepositPercentage(),
                reservation.getDepositAmount(),
                reservation.isDepositPaid(),
                reservation.getTotalAmount(),
                reservation.getTotalPaid(),
                reservation.getReservationDate(),
                reservation.getPickupDeadline(),
                reservation.getPickupDate(),
                reservation.getDueDate(),
                reservation.getReturnDate(),
                reservation.getLateFeePerDay(),
                reservation.getLateFeeTotal(),
                reservation.getRenewalCount(),
                reservation.getMaxRenewals(),
                reservation.getNotes(),
                reservation.getCreatedAt(),
                reservation.getUpdatedAt());
    }
}
