package com.library.reservation.application.service.reservation;

import com.library.iam.domain.model.NotificationEvent;
import com.library.iam.domain.port.out.NotificationService;
import com.library.reservation.application.dto.command.reservation.CreateReservationCommand;
import com.library.reservation.application.dto.response.reservation.ReservationResponseDTO;
import com.library.reservation.application.validation.ReservationValidator;
import com.library.reservation.domain.model.Reservation;
import com.library.reservation.domain.model.ReservationStatus;
import com.library.reservation.domain.port.out.ReservationRepository;
import com.library.kernel.generation.CodeGenerationService;
import com.library.kernel.loan.LoanPolicy;
import com.library.stock.domain.model.StockItem;
import com.library.stock.domain.port.out.StockItemRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class CreateReservationUseCase {

    private final ReservationRepository reservationRepository;
    private final ReservationValidator validator;
    private final CodeGenerationService codeGenerationService;
    private final LoanPolicy loanPolicy;
    private final StockItemRepository stockItemRepository;
    private final NotificationService notificationService;

    public CreateReservationUseCase(ReservationRepository reservationRepository,
                                    ReservationValidator validator,
                                    CodeGenerationService codeGenerationService,
                                    LoanPolicy loanPolicy,
                                    StockItemRepository stockItemRepository,
                                    NotificationService notificationService) {
        this.reservationRepository = reservationRepository;
        this.validator = validator;
        this.codeGenerationService = codeGenerationService;
        this.loanPolicy = loanPolicy;
        this.stockItemRepository = stockItemRepository;
        this.notificationService = notificationService;
    }

    public ReservationResponseDTO execute(CreateReservationCommand command) {
        return execute(command, null);
    }

    public ReservationResponseDTO execute(CreateReservationCommand command, Long stockItemId) {
        String code = codeGenerationService.generate("RES");
        while (reservationRepository.findByCode(code).isPresent()) {
            code = codeGenerationService.generate("RES");
        }

        int loanDays = command.loanDays() != null ? command.loanDays() : 7;

        StockItem selectedItem;
        if (stockItemId != null) {
            selectedItem = stockItemRepository.findById(stockItemId)
                    .orElseThrow(() -> new IllegalArgumentException("Stock item not found"));
            if (selectedItem.getState() != com.library.stock.domain.model.StockItemState.AVAILABLE) {
                throw new IllegalStateException("Stock item is not available");
            }
        } else {
            List<StockItem> availableItems = stockItemRepository.findAvailableByEditionId(command.editionId());
            if (availableItems.isEmpty()) {
                throw new IllegalStateException("No available stock items for this edition");
            }
            selectedItem = availableItems.get(0);
        }

        BigDecimal dailyPrice = selectedItem.getDailyPrice() != null ? selectedItem.getDailyPrice() : BigDecimal.TEN;
        BigDecimal totalAmount = dailyPrice.multiply(BigDecimal.valueOf(loanDays));

        int depositPercentage = 50;
        BigDecimal depositAmount = totalAmount
                .multiply(BigDecimal.valueOf(depositPercentage))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        LocalDate pickupDeadline = LocalDate.now().plusDays(3);
        LocalDate dueDate = LocalDate.now().plusDays(loanDays);

        Reservation reservation = Reservation.withoutId(
                code,
                command.clientId(),
                selectedItem.getEditionId(),
                selectedItem.getId(),
                ReservationStatus.DEPOSIT_PENDING,
                depositPercentage,
                depositAmount,
                totalAmount,
                pickupDeadline,
                dueDate,
                dailyPrice,
                loanPolicy.maxRenewals("CASUAL"));

        reservation.setNotes(command.notes());
        reservation.setReservationDate(Instant.now());

        validator.validate(reservation);
        Reservation saved = reservationRepository.save(reservation);

        selectedItem.setReservationId(saved.getId());
        selectedItem.setState(com.library.stock.domain.model.StockItemState.RESERVED);
        stockItemRepository.save(selectedItem);

        // Notify connected employees about the new reservation
        if (notificationService != null) {
            String message = "New reservation " + saved.getCode() + " created for " + selectedItem.getCode();
            NotificationEvent event = NotificationEvent.of(
                    "reservation.created",
                    message,
                    saved.getClientId(),
                    "System",
                    saved.getId().toString());
            notificationService.publish(event);
        }

        return ReservationResponseDTO.of(saved);
    }
}
