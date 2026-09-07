package com.library.reservation.domain.exception;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(Long id) {
        super("Reservation not found: " + id);
    }

    public ReservationNotFoundException(String message) {
        super(message);
    }
}
