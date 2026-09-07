package com.library.reservation.application.service.reservation;

import com.library.reservation.domain.model.Reservation;
import com.library.reservation.domain.model.ReservationStatus;
import com.library.reservation.domain.port.out.ReservationRepository;

import java.time.LocalDate;
import java.util.List;

public class ExpireReservationsUseCase {

    private final ReservationRepository reservationRepository;

    public ExpireReservationsUseCase(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public int execute() {
        List<Reservation> pendingReservations = reservationRepository.findByStatus(ReservationStatus.DEPOSIT_PENDING);
        int expiredCount = 0;

        for (Reservation reservation : pendingReservations) {
            if (reservation.getPickupDeadline() != null && LocalDate.now().isAfter(reservation.getPickupDeadline())) {
                reservation.setStatus(ReservationStatus.CANCELLED);
                reservationRepository.save(reservation);
                expiredCount++;
            }
        }

        return expiredCount;
    }
}
