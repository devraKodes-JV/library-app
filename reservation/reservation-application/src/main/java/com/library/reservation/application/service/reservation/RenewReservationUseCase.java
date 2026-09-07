package com.library.reservation.application.service.reservation;

import com.library.reservation.application.dto.command.reservation.RenewReservationCommand;
import com.library.reservation.domain.exception.ReservationNotFoundException;
import com.library.reservation.domain.model.Reservation;
import com.library.reservation.domain.model.ReservationStatus;
import com.library.reservation.domain.port.out.ReservationRepository;
import com.library.kernel.loan.LoanPolicy;

import java.time.LocalDate;

public class RenewReservationUseCase {

    private final ReservationRepository reservationRepository;
    private final LoanPolicy loanPolicy;

    public RenewReservationUseCase(ReservationRepository reservationRepository,
                                   LoanPolicy loanPolicy) {
        this.reservationRepository = reservationRepository;
        this.loanPolicy = loanPolicy;
    }

    public void execute(RenewReservationCommand command) {
        Reservation existing = reservationRepository.findById(command.id())
                .orElseThrow(() -> new ReservationNotFoundException(command.id()));

        if (existing.getStatus() != ReservationStatus.ACTIVE) {
            throw new IllegalStateException("Only active reservations can be renewed.");
        }

        if (existing.getRenewalCount() >= existing.getMaxRenewals()) {
            throw new IllegalStateException("Maximum renewals reached.");
        }

        int renewalDays = loanPolicy.maxLoanDays("CASUAL");
        existing.setDueDate(existing.getDueDate().plusDays(renewalDays));
        existing.setRenewalCount(existing.getRenewalCount() + 1);
        reservationRepository.save(existing);
    }
}
