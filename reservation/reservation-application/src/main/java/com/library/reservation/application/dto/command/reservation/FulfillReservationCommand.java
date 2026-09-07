package com.library.reservation.application.dto.command.reservation;

public record FulfillReservationCommand(Long id, String paymentMethod, String receivedBy, boolean paymentConfirmed) {
}
