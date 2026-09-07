package com.library.reservation.application.dto.command.reservation;

public record ReturnReservationCommand(Long id, String paymentMethod, String receivedBy, boolean paymentConfirmed) {
}
