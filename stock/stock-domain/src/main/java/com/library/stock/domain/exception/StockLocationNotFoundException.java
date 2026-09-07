package com.library.stock.domain.exception;

public class StockLocationNotFoundException extends RuntimeException {
    public StockLocationNotFoundException(Long id) {
        super("StockLocation not found: " + id);
    }
}
