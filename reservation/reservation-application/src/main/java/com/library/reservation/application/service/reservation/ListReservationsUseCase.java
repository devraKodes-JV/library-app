package com.library.reservation.application.service.reservation;

import com.library.reservation.application.dto.response.reservation.ReservationResponseDTO;
import com.library.reservation.domain.port.out.ReservationRepository;

import java.util.List;

public class ListReservationsUseCase {

    private final ReservationRepository reservationRepository;

    public ListReservationsUseCase(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationResponseDTO> execute() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponseDTO::of)
                .toList();
    }

    public List<ReservationResponseDTO> execute(String status) {
        return reservationRepository.findAll(status).stream()
                .map(ReservationResponseDTO::of)
                .toList();
    }
}
