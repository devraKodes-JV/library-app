package com.library.stock.application.service.stockitem;

import java.math.BigDecimal;
import java.util.Map;

import com.library.kernel.generation.CodeGenerationService;
import com.library.stock.application.dto.StockItemDTO;
import com.library.stock.application.dto.command.stockitem.CreateStockItemCommand;
import com.library.stock.domain.model.StockItem;
import com.library.stock.domain.model.StockItemCondition;
import com.library.stock.domain.model.StockItemState;
import com.library.stock.domain.port.out.StockItemRepository;
import com.library.stock.domain.exception.ValidationException;

public class CreateStockItemUseCase {

    private final StockItemRepository stockItemRepository;
    private final CodeGenerationService codeGenerationService;

    public CreateStockItemUseCase(StockItemRepository stockItemRepository,
                                  CodeGenerationService codeGenerationService) {
        this.stockItemRepository = stockItemRepository;
        this.codeGenerationService = codeGenerationService;
    }

    public StockItemDTO execute(CreateStockItemCommand command) {
        if (command.editionId() == null) {
            throw new ValidationException(Map.of("editionId", "Edition is required"));
        }
        if (command.locationId() == null) {
            throw new ValidationException(Map.of("locationId", "Location is required"));
        }

        String code = codeGenerationService.generate("STOCK");
        while (stockItemRepository.findByCode(code).isPresent()) {
            code = codeGenerationService.generate("STOCK");
        }

        StockItemState state = command.state() != null ? StockItemState.valueOf(command.state()) : StockItemState.AVAILABLE;
        StockItemCondition condition = command.condition() != null ? StockItemCondition.fromLabel(command.condition()) : StockItemCondition.GOOD;
        BigDecimal dailyPrice = command.dailyPrice() != null ? command.dailyPrice() : BigDecimal.TEN;

        StockItem stockItem = StockItem.withoutId(code, command.editionId(), command.locationId(), state, condition, dailyPrice);
        StockItem saved = stockItemRepository.save(stockItem);
        return StockItemDTO.of(saved);
    }
}
