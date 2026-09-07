package com.library.reservation.application.service.reservation;

import com.library.reservation.application.dto.command.reservation.FulfillReservationCommand;
import com.library.reservation.domain.exception.ReservationNotFoundException;
import com.library.reservation.domain.model.Reservation;
import com.library.reservation.domain.model.ReservationStatus;
import com.library.reservation.domain.port.out.PaymentRecorder;
import com.library.reservation.domain.port.out.ReservationRepository;
import com.library.stock.domain.model.StockItemState;
import com.library.stock.domain.port.out.StockItemRepository;

import java.math.BigDecimal;
import java.time.Instant;

public class FulfillReservationUseCase {

    private final ReservationRepository reservationRepository;
    private final StockItemRepository stockItemRepository;
    private final PaymentRecorder paymentRecorder;

    public FulfillReservationUseCase(ReservationRepository reservationRepository,
                                     StockItemRepository stockItemRepository,
                                     PaymentRecorder paymentRecorder) {
        this.reservationRepository = reservationRepository;
        this.stockItemRepository = stockItemRepository;
        this.paymentRecorder = paymentRecorder;
    }

    public void execute(FulfillReservationCommand command) {
        if (!command.paymentConfirmed()) {
            throw new IllegalStateException("Payment must be confirmed by the employee before processing the fulfillment.");
        }
        Reservation existing = reservationRepository.findById(command.id())
                .orElseThrow(() -> new ReservationNotFoundException(command.id()));

        if (existing.getStatus() != ReservationStatus.DEPOSIT_PENDING) {
            throw new IllegalStateException("Reservation must be in DEPOSIT_PENDING status to fulfill.");
        }

        existing.setStatus(ReservationStatus.ACTIVE);
        existing.setDepositPaid(true);
        existing.setPickupDate(Instant.now());
        reservationRepository.save(existing);

        if (existing.getStockItemId() != null) {
            stockItemRepository.findById(existing.getStockItemId()).ifPresent(item -> {
                item.setState(StockItemState.BORROWED);
                item.setReservationId(existing.getId());
                stockItemRepository.save(item);
            });
        }

        BigDecimal deposit = existing.getDepositAmount() != null ? existing.getDepositAmount() : BigDecimal.ZERO;
        if (deposit.signum() > 0) {
            paymentRecorder.recordDeposit(
                    existing.getId(),
                    existing.getClientId(),
                    deposit,
                    command.paymentMethod() != null ? command.paymentMethod() : "CASH",
                    command.receivedBy());
        }
    }
}
