package com.library.reservation.domain.model;

public enum ReservationStatus {

    DEPOSIT_PENDING("Deposit Pending", "Waiting for deposit payment"),
    ACTIVE("Active", "Picked up, within due date"),
    OVERDUE("OVERDUE", "Past due date, late fees applying"),
    RETURNED("Returned", "Successfully returned"),
    CANCELLED("Cancelled", "Cancelled or expired");

    private final String label;
    private final String description;

    ReservationStatus(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() { return label; }
    public String getDescription() { return description; }

    public boolean isActive() {
        return this == ACTIVE || this == OVERDUE;
    }

    public boolean canTransitionTo(ReservationStatus newStatus) {
        return switch (this) {
            case DEPOSIT_PENDING -> newStatus == ACTIVE || newStatus == CANCELLED;
            case ACTIVE -> newStatus == OVERDUE || newStatus == RETURNED;
            case OVERDUE -> newStatus == RETURNED;
            case RETURNED, CANCELLED -> false;
        };
    }
}
