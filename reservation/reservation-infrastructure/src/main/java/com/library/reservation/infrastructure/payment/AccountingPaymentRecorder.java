package com.library.reservation.infrastructure.payment;

import com.library.accounting.application.service.payment.RecordClientPaymentUseCase;
import com.library.reservation.domain.port.out.PaymentRecorder;

import java.math.BigDecimal;

public class AccountingPaymentRecorder implements PaymentRecorder {

    private final RecordClientPaymentUseCase recordClientPaymentUseCase;

    public AccountingPaymentRecorder(RecordClientPaymentUseCase recordClientPaymentUseCase) {
        this.recordClientPaymentUseCase = recordClientPaymentUseCase;
    }

    @Override
    public void recordDeposit(Long reservationId, Long clientId, BigDecimal amount,
                              String paymentMethod, String receivedBy) {
        recordClientPaymentUseCase.execute(
                amount, paymentMethod, "DEPOSIT",
                clientId, "RESERVATION_DEPOSIT", reservationId,
                "Deposit for reservation #" + reservationId, receivedBy);
    }

    @Override
    public void recordLoanSettlement(Long reservationId, Long clientId, BigDecimal amount,
                                     String paymentMethod, String receivedBy) {
        recordClientPaymentUseCase.execute(
                amount, paymentMethod, "LOAN_SETTLEMENT",
                clientId, "RESERVATION_SETTLEMENT", reservationId,
                "Loan settlement for reservation #" + reservationId, receivedBy);
    }

    @Override
    public void recordLateFee(Long reservationId, Long clientId, BigDecimal amount,
                              String paymentMethod, String receivedBy) {
        recordClientPaymentUseCase.execute(
                amount, paymentMethod, "LATE_FEE",
                clientId, "RESERVATION_LATE_FEE", reservationId,
                "Late fee for reservation #" + reservationId, receivedBy);
    }
}
