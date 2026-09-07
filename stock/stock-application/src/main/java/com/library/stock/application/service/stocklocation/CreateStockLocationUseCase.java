package com.library.stock.application.service.stocklocation;

import java.util.Map;

import com.library.kernel.generation.CodeGenerationService;
import com.library.stock.application.dto.StockLocationDTO;
import com.library.stock.application.dto.command.stocklocation.CreateStockLocationCommand;
import com.library.stock.domain.model.StockLocation;
import com.library.stock.domain.port.out.StockLocationRepository;
import com.library.stock.domain.exception.ValidationException;

public class CreateStockLocationUseCase {

    private final StockLocationRepository stockLocationRepository;
    private final CodeGenerationService codeGenerationService;

    public CreateStockLocationUseCase(StockLocationRepository stockLocationRepository,
                                      CodeGenerationService codeGenerationService) {
        this.stockLocationRepository = stockLocationRepository;
        this.codeGenerationService = codeGenerationService;
    }

    public StockLocationDTO execute(CreateStockLocationCommand command) {
        if (command.name() == null || command.name().isBlank()) {
            throw new ValidationException(Map.of("name", "Name is required"));
        }

        String code = codeGenerationService.generate("LOC");
        while (stockLocationRepository.findByCode(code).isPresent()) {
            code = codeGenerationService.generate("LOC");
        }

        StockLocation location = StockLocation.withoutId(code, command.name(), command.description(), command.capacity());
        StockLocation saved = stockLocationRepository.save(location);
        return StockLocationDTO.of(saved);
    }
}
