package com.library.stock.domain.exception;

public class StockItemNotFoundException extends RuntimeException {
    public StockItemNotFoundException(Long id) {
        super("StockItem not found: " + id);
    }

    public StockItemNotFoundException(String message) {
        super(message);
    }
}
