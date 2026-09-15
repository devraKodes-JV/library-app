package com.library.reservation.application.service.reservation;

import java.util.List;

import com.library.books.domain.model.Work;
import com.library.books.domain.port.out.EditionRepository;
import com.library.books.domain.port.out.WorkRepository;
import com.library.client.domain.port.out.ClientRepository;
import com.library.reservation.application.dto.response.reservation.ReservationResponseDTO;
import com.library.reservation.domain.port.out.ReservationRepository;

public class ListReservationsUseCase {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final EditionRepository editionRepository;
    private final WorkRepository workRepository;

    public ListReservationsUseCase(ReservationRepository reservationRepository,
                                   ClientRepository clientRepository,
                                   EditionRepository editionRepository,
                                   WorkRepository workRepository) {
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
        this.editionRepository = editionRepository;
        this.workRepository = workRepository;
    }

    public List<ReservationResponseDTO> execute() {
        return reservationRepository.findAll().stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<ReservationResponseDTO> execute(String status) {
        return reservationRepository.findAll(status).stream()
                .map(this::toResponseDto)
                .toList();
    }

    private ReservationResponseDTO toResponseDto(com.library.reservation.domain.model.Reservation reservation) {
        String clientName = null;
        String clientDni = null;
        boolean clientDataIncomplete = false;

        var clientOptional = clientRepository.findById(reservation.getClientId());
        if (clientOptional.isPresent()) {
            var client = clientOptional.get();
            clientName = client.getFullName();
            clientDni = client.getDni();
            clientDataIncomplete = client.getFullName() == null || client.getFullName().isBlank();
        }

        String editionName = null;
        var editionOptional = editionRepository.findById(reservation.getEditionId());
        if (editionOptional.isPresent()) {
            var edition = editionOptional.get();
            String workTitle = workRepository.findById(edition.getWorkId())
                    .map(Work::getTitle)
                    .orElse(null);
            if (workTitle != null && edition.getEditionNumber() != null) {
                editionName = workTitle + " - " + edition.getEditionNumber();
            } else if (workTitle != null) {
                editionName = workTitle;
            } else {
                editionName = edition.getEditionNumber();
            }
        }

        return ReservationResponseDTO.of(reservation, clientName, clientDni, clientDataIncomplete, editionName);
    }
}
