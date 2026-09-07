package com.library.reservation.application.service.reservation;

import com.library.reservation.domain.port.out.PaymentRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class NoopPaymentRecorder implements PaymentRecorder {

    private static final Logger log = LoggerFactory.getLogger(NoopPaymentRecorder.class);

    @Override
    public void recordDeposit(Long reservationId, Long clientId, BigDecimal amount,
                              String paymentMethod, String receivedBy) {
        log.info("[noop] Deposit recorded for reservation={} client={} amount={} method={} by={}",
                reservationId, clientId, amount, paymentMethod, receivedBy);
    }

    @Override
    public void recordLoanSettlement(Long reservationId, Long clientId, BigDecimal amount,
                                     String paymentMethod, String receivedBy) {
        log.info("[noop] Settlement recorded for reservation={} client={} amount={} method={} by={}",
                reservationId, clientId, amount, paymentMethod, receivedBy);
    }

    @Override
    public void recordLateFee(Long reservationId, Long clientId, BigDecimal amount,
                              String paymentMethod, String receivedBy) {
        log.info("[noop] Late fee recorded for reservation={} client={} amount={} method={} by={}",
                reservationId, clientId, amount, paymentMethod, receivedBy);
    }
}
