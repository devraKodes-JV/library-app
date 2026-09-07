package com.library.stock.domain.model;

public enum StockItemState {
    AVAILABLE("Available", 1),
    RESERVED("Reserved", 2),
    BORROWED("Borrowed", 3),
    REVIEW("In Review", 4),
    DAMAGED("Damaged", 5),
    RETIRED("Retired", 6);

    private final String label;
    private final int order;

    StockItemState(String label, int order) {
        this.label = label;
        this.order = order;
    }

    public String getLabel() {
        return label;
    }

    public int getOrder() {
        return order;
    }

    public boolean isActive() {
        return this != RETIRED;
    }

    public boolean isMovable() {
        return this == AVAILABLE || this == REVIEW;
    }
}
