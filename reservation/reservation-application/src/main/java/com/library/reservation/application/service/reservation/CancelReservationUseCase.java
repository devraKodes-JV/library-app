package com.library.reservation.application.service.reservation;

import com.library.reservation.application.dto.command.reservation.CancelReservationCommand;
import com.library.reservation.domain.exception.ReservationNotFoundException;
import com.library.reservation.domain.model.Reservation;
import com.library.reservation.domain.model.ReservationStatus;
import com.library.reservation.domain.port.out.ReservationRepository;

public class CancelReservationUseCase {

    private final ReservationRepository reservationRepository;

    public CancelReservationUseCase(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void execute(CancelReservationCommand command) {
        Reservation existing = reservationRepository.findById(command.id())
                .orElseThrow(() -> new ReservationNotFoundException(command.id()));

        if (!existing.getStatus().canTransitionTo(ReservationStatus.CANCELLED)) {
            throw new IllegalStateException("Cannot cancel reservation in status: " + existing.getStatus());
        }

        existing.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(existing);
    }
}
