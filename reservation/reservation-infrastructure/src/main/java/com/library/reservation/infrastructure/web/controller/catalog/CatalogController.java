package com.library.reservation.infrastructure.web.controller.catalog;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.application.service.edition.ListEditionsUseCase;
import com.library.stock.domain.port.out.StockItemRepository;

import io.javalin.http.Context;

public class CatalogController {

    private final ListEditionsUseCase listEditionsUseCase;
    private final StockItemRepository stockItemRepository;

    public CatalogController(ListEditionsUseCase listEditionsUseCase, StockItemRepository stockItemRepository) {
        this.listEditionsUseCase = listEditionsUseCase;
        this.stockItemRepository = stockItemRepository;
    }

    public void showCatalog(Context ctx) {
        List<EditionResponseDTO> editions = listEditionsUseCase.execute("all");

        record CatalogItem(Long id, String title, String editionNumber, String publisher, int available) {}

        List<CatalogItem> catalogItems = editions.stream()
                .map(edition -> {
                    int available = stockItemRepository.findAvailableByEditionId(edition.id()).size();
                    return new CatalogItem(
                            edition.id(),
                            edition.workTitle() != null ? edition.workTitle() : "Unknown",
                            edition.editionNumber() != null ? edition.editionNumber() : "",
                            edition.publisherName() != null ? edition.publisherName() : "",
                            available);
                })
                .filter(item -> item.available() > 0)
                .toList();

        ctx.render("reservation/catalog/view", Map.of("catalogItems", catalogItems));
    }
}
