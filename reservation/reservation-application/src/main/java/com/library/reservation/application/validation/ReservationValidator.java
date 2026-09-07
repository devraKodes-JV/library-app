package com.library.reservation.application.validation;

import com.library.reservation.domain.model.Reservation;
import com.library.kernel.validation.Validator;

public class ReservationValidator implements Validator<Reservation> {

    @Override
    public void validate(Reservation reservation) {
        var errors = new java.util.LinkedHashMap<String, String>();

        if (reservation.getClientId() == null) {
            errors.put("clientId", "Client is required.");
        }

        if (reservation.getEditionId() == null) {
            errors.put("editionId", "Edition is required.");
        }

        if (reservation.getTotalAmount() == null || reservation.getTotalAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            errors.put("totalAmount", "Total amount must be greater than zero.");
        }

        if (reservation.getDepositPercentage() == null || reservation.getDepositPercentage() < 0 || reservation.getDepositPercentage() > 100) {
            errors.put("depositPercentage", "Deposit percentage must be between 0 and 100.");
        }

        if (!errors.isEmpty()) {
            throw new com.library.reservation.domain.exception.ValidationException(errors);
        }
    }
}
