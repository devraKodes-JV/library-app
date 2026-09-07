package com.library.reservation.application.dto.command.reservation;

public record CreateReservationCommand(
        Long clientId,
        Long editionId,
        Integer loanDays,
        String notes) {
}
