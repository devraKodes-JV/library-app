package com.library.reservation.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import com.library.reservation.domain.model.Reservation;
import com.library.reservation.domain.model.ReservationStatus;
import com.library.reservation.domain.port.out.ReservationRepository;
import com.library.reservation.infrastructure.persistence.entity.ReservationEntity;
import com.library.reservation.infrastructure.persistence.mapper.ReservationMapper;
import com.library.reservation.infrastructure.persistence.repository.jpa.ReservationJpaRepository;

public class ReservationPersistenceAdapter implements ReservationRepository {

    private final ReservationJpaRepository<ReservationEntity, Long> reservationJpaRepository;

    public ReservationPersistenceAdapter(ReservationJpaRepository<ReservationEntity, Long> reservationJpaRepository) {
        this.reservationJpaRepository = reservationJpaRepository;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservationJpaRepository.findById(id)
                .map(ReservationMapper::toDomain);
    }

    @Override
    public Optional<Reservation> findByIdIncludingDeleted(Long id) {
        return reservationJpaRepository.findByIdIncludingDeleted(id)
                .map(ReservationMapper::toDomain);
    }

    @Override
    public Optional<Reservation> findByCode(String code) {
        return reservationJpaRepository.findByCode(code)
                .map(ReservationMapper::toDomain);
    }

    @Override
    public List<Reservation> findAll() {
        return reservationJpaRepository.findAll().stream()
                .map(ReservationMapper::toDomain)
                .toList();
    }

    @Override
    public List<Reservation> findAll(String status) {
        return reservationJpaRepository.findAll(status).stream()
                .map(ReservationMapper::toDomain)
                .toList();
    }

    @Override
    public List<Reservation> findByClientId(Long clientId) {
        return reservationJpaRepository.findByClientId(clientId).stream()
                .map(ReservationMapper::toDomain)
                .toList();
    }

    @Override
    public List<Reservation> findByEditionId(Long editionId) {
        return reservationJpaRepository.findByEditionId(editionId).stream()
                .map(ReservationMapper::toDomain)
                .toList();
    }

    @Override
    public List<Reservation> findByStatus(ReservationStatus status) {
        return reservationJpaRepository.findByStatus(status).stream()
                .map(ReservationMapper::toDomain)
                .toList();
    }

    @Override
    public long countActiveByClientId(Long clientId) {
        return reservationJpaRepository.countActiveByClientId(clientId);
    }

    @Override
    public Reservation save(Reservation reservation) {
        ReservationEntity entity = ReservationMapper.toEntity(reservation);
        ReservationEntity saved = reservationJpaRepository.save(entity);
        return ReservationMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        reservationJpaRepository.deleteById(id);
    }

    @Override
    public void reactivateById(Long id) {
        reservationJpaRepository.reactivateById(id);
    }
}
