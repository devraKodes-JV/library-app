package com.library.stock.application.service.stockitem;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.library.stock.application.dto.StockItemDTO;
import com.library.stock.domain.model.StockItem;
import com.library.stock.domain.port.out.StockItemRepository;
import com.library.stock.domain.port.out.StockLocationRepository;

public class ListStockItemsUseCase {

    private final StockItemRepository stockItemRepository;
    private final StockLocationRepository stockLocationRepository;

    public ListStockItemsUseCase(StockItemRepository stockItemRepository,
                                 StockLocationRepository stockLocationRepository) {
        this.stockItemRepository = stockItemRepository;
        this.stockLocationRepository = stockLocationRepository;
    }

    public List<StockItemDTO> execute() {
        return execute("active", null);
    }

    public List<StockItemDTO> execute(String status) {
        return execute(status, null);
    }

    public List<StockItemDTO> execute(String status, Map<Long, String> editionNames) {
        Map<Long, String> locationNames = stockLocationRepository.findAll("active").stream()
                .collect(Collectors.toMap(
                        loc -> loc.getId(),
                        loc -> loc.getName() != null ? loc.getName() : ""));

        return stockItemRepository.findAll(status).stream()
                .map(item -> {
                    String locName = locationNames.getOrDefault(item.getLocationId(), "");
                    String edName = editionNames != null ?
                            editionNames.getOrDefault(item.getEditionId(), "") : null;
                    return StockItemDTO.of(item, edName, locName);
                })
                .toList();
    }
}
