package com.library.reservation.domain.exception;

public class PickupDeadlineExpiredException extends RuntimeException {
    public PickupDeadlineExpiredException(String message) {
        super(message);
    }
}
