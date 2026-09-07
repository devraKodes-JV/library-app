package com.library.client.domain.model;

import java.time.Instant;
import java.time.LocalDate;

public class Client {

    private Long id;
    private String code;
    private String dni;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private ClientType type;
    private ClientStatus status;
    private LocalDate memberSince;
    private LocalDate memberUntil;
    private LocalDate birthDate;
    private boolean membershipPaid;
    private String notes;
    private boolean enabled = true;
    private Instant createdAt;
    private Instant updatedAt;

    public Client(Long id, String code, String dni, String fullName, String email, String phone,
                  String address, ClientType type, ClientStatus status, LocalDate memberSince,
                  LocalDate memberUntil, LocalDate birthDate, boolean membershipPaid, String notes,
                  boolean enabled, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.code = code;
        this.dni = dni;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.type = type;
        this.status = status;
        this.memberSince = memberSince;
        this.memberUntil = memberUntil;
        this.birthDate = birthDate;
        this.membershipPaid = membershipPaid;
        this.notes = notes;
        this.enabled = enabled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Client withoutId(String code, String dni, String fullName, String email, String phone,
                                   String address, ClientType type, ClientStatus status, LocalDate memberSince,
                                   LocalDate memberUntil, LocalDate birthDate, String notes) {
        return new Client(null, code, dni, fullName, email, phone, address, type, status,
                memberSince, memberUntil, birthDate, false, notes, true, null, null);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public ClientType getType() { return type; }
    public void setType(ClientType type) { this.type = type; }

    public ClientStatus getStatus() { return status; }
    public void setStatus(ClientStatus status) { this.status = status; }

    public LocalDate getMemberSince() { return memberSince; }
    public void setMemberSince(LocalDate memberSince) { this.memberSince = memberSince; }

    public LocalDate getMemberUntil() { return memberUntil; }
    public void setMemberUntil(LocalDate memberUntil) { this.memberUntil = memberUntil; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public boolean isMembershipPaid() { return membershipPaid; }
    public void setMembershipPaid(boolean membershipPaid) { this.membershipPaid = membershipPaid; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
