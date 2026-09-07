package com.library.client.domain.model;

public enum ClientStatus {

    ACTIVE("Active", "Client is active and can make reservations"),
    SUSPENDED("Suspended", "Client blocked — pending fees or lost books"),
    EXPIRED("Expired", "Member subscription expired");

    private final String label;
    private final String description;

    ClientStatus(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() { return label; }
    public String getDescription() { return description; }
}
