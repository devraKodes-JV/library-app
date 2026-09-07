package com.library.reservation.application.service.reservation;

import com.library.reservation.application.dto.command.reservation.ReturnReservationCommand;
import com.library.reservation.domain.exception.ReservationNotFoundException;
import com.library.reservation.domain.model.Reservation;
import com.library.reservation.domain.model.ReservationStatus;
import com.library.reservation.domain.port.out.PaymentRecorder;
import com.library.reservation.domain.port.out.ReservationRepository;
import com.library.stock.domain.model.StockItemState;
import com.library.stock.domain.port.out.StockItemRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReturnReservationUseCase {

    private final ReservationRepository reservationRepository;
    private final StockItemRepository stockItemRepository;
    private final PaymentRecorder paymentRecorder;

    public ReturnReservationUseCase(ReservationRepository reservationRepository,
                                    StockItemRepository stockItemRepository,
                                    PaymentRecorder paymentRecorder) {
        this.reservationRepository = reservationRepository;
        this.stockItemRepository = stockItemRepository;
        this.paymentRecorder = paymentRecorder;
    }

    public void execute(ReturnReservationCommand command) {
        if (!command.paymentConfirmed()) {
            throw new IllegalStateException("Payment must be confirmed by the employee before processing the return.");
        }
        Reservation existing = reservationRepository.findById(command.id())
                .orElseThrow(() -> new ReservationNotFoundException(command.id()));

        if (existing.getStatus() != ReservationStatus.ACTIVE && existing.getStatus() != ReservationStatus.OVERDUE) {
            throw new IllegalStateException("Reservation must be active or overdue to return.");
        }

        ReturnBreakdown breakdown = preview(existing);
        existing.setLateFeeTotal(breakdown.lateFee());
        existing.setStatus(ReservationStatus.RETURNED);
        existing.setReturnDate(Instant.now());
        BigDecimal total = existing.getTotalAmount() != null ? existing.getTotalAmount() : BigDecimal.ZERO;
        existing.setTotalPaid(total.add(breakdown.lateFee()));
        reservationRepository.save(existing);

        if (existing.getStockItemId() != null) {
            stockItemRepository.findById(existing.getStockItemId()).ifPresent(item -> {
                item.setState(StockItemState.AVAILABLE);
                item.setReservationId(null);
                stockItemRepository.save(item);
            });
        }

        String method = command.paymentMethod() != null ? command.paymentMethod() : "CASH";
        String by = command.receivedBy();

        if (breakdown.lateFee().signum() > 0) {
            paymentRecorder.recordLateFee(existing.getId(), existing.getClientId(),
                    breakdown.lateFee(), method, by);
        }
        if (breakdown.settlement().signum() > 0) {
            paymentRecorder.recordLoanSettlement(existing.getId(), existing.getClientId(),
                    breakdown.settlement(), method, by);
        }
    }

    public ReturnBreakdown preview(Reservation reservation) {
        BigDecimal lateFee = calculateLateFee(reservation);
        BigDecimal total = reservation.getTotalAmount() != null ? reservation.getTotalAmount() : BigDecimal.ZERO;
        BigDecimal deposit = reservation.getDepositAmount() != null ? reservation.getDepositAmount() : BigDecimal.ZERO;
        BigDecimal settlement = total.subtract(deposit).add(lateFee);
        if (settlement.signum() < 0) settlement = BigDecimal.ZERO;
        return new ReturnBreakdown(lateFee, deposit, settlement);
    }

    public ReturnBreakdown previewById(Long id) {
        Reservation existing = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        return preview(existing);
    }

    private BigDecimal calculateLateFee(Reservation reservation) {
        if (reservation.getDueDate() == null) {
            return BigDecimal.ZERO;
        }

        LocalDate today = LocalDate.now();
        if (!today.isAfter(reservation.getDueDate())) {
            return BigDecimal.ZERO;
        }

        long daysLate = ChronoUnit.DAYS.between(reservation.getDueDate(), today);
        BigDecimal lateFeePerDay = reservation.getLateFeePerDay() != null ? reservation.getLateFeePerDay() : BigDecimal.TEN;
        return lateFeePerDay.multiply(BigDecimal.valueOf(daysLate)).setScale(2, RoundingMode.HALF_UP);
    }

    public record ReturnBreakdown(BigDecimal lateFee, BigDecimal deposit, BigDecimal settlement) {
    }
}
