package com.library.reservation.application.service.reservation;

import com.library.books.domain.port.out.EditionRepository;
import com.library.books.domain.port.out.WorkRepository;
import com.library.client.domain.model.Client;
import com.library.client.domain.port.out.ClientRepository;
import com.library.reservation.application.dto.response.reservation.ReservationResponseDTO;
import com.library.reservation.domain.exception.ReservationNotFoundException;
import com.library.reservation.domain.model.Reservation;
import com.library.reservation.domain.port.out.ReservationRepository;

import java.util.Map;
import java.util.Optional;

public class GetReservationUseCase {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final EditionRepository editionRepository;
    private final WorkRepository workRepository;

    public GetReservationUseCase(ReservationRepository reservationRepository,
                                 ClientRepository clientRepository,
                                 EditionRepository editionRepository,
                                 WorkRepository workRepository) {
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
        this.editionRepository = editionRepository;
        this.workRepository = workRepository;
    }

    public ReservationResponseDTO execute(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));

        String clientName = null;
        String clientDni = null;
        boolean clientDataIncomplete = false;
        if (reservation.getClientId() != null) {
            Optional<Client> clientOpt = clientRepository.findById(reservation.getClientId());
            if (clientOpt.isPresent()) {
                Client client = clientOpt.get();
                String fullName = client.getFullName();
                if (fullName == null || fullName.isBlank()) {
                    clientDataIncomplete = true;
                } else {
                    clientName = fullName;
                }
                clientDni = client.getDni();
            } else {
                clientDataIncomplete = true;
            }
        }

        String editionName = null;
        if (reservation.getEditionId() != null) {
            var editionOpt = editionRepository.findById(reservation.getEditionId());
            if (editionOpt.isPresent()) {
                var edition = editionOpt.get();
                if (edition.getWorkId() != null) {
                    var workOpt = workRepository.findById(edition.getWorkId());
                    editionName = workOpt.map(w -> w.getTitle()).orElse(null);
                }
            }
        }

        return ReservationResponseDTO.of(reservation, clientName, clientDni, clientDataIncomplete, editionName);
    }
}
