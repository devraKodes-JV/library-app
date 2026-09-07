package com.library.stock.domain.model;

public enum StockItemCondition {
    GOOD("Good", 3),
    WORN("Worn", 2),
    DAMAGED("Damaged", 1);

    private final String label;
    private final int severity;

    StockItemCondition(String label, int severity) {
        this.label = label;
        this.severity = severity;
    }

    public String getLabel() {
        return label;
    }

    public int getSeverity() {
        return severity;
    }

    public static StockItemCondition fromLabel(String label) {
        for (StockItemCondition c : values()) {
            if (c.label.equalsIgnoreCase(label)) {
                return c;
            }
        }
        return null;
    }
}
