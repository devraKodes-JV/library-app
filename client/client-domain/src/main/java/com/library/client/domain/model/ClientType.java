package com.library.client.domain.model;

public enum ClientType {

    CASUAL("Casual", "Walk-in client — minimal data, no benefits"),
    MEMBER("Member", "Registered member — extended loan period, reservation priority");

    private final String label;
    private final String description;

    ClientType(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() { return label; }
    public String getDescription() { return description; }
}
