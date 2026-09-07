package com.library.stock.domain.model;

public enum MovementType {
    ENTRY("Entry"),
    EXIT("Exit"),
    TRANSFER("Transfer");

    private final String label;

    MovementType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
