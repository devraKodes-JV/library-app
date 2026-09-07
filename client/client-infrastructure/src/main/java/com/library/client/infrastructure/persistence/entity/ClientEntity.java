package com.library.client.infrastructure.persistence.entity;

import java.io.Serializable;
import java.time.LocalDate;

import org.hibernate.envers.Audited;

import com.library.iam.infrastructure.persistence.entity.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clients")
@Audited
public class ClientEntity extends AuditableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 20)
    private String dni;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(length = 150)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 300)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private com.library.client.domain.model.ClientType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private com.library.client.domain.model.ClientStatus status;

    @Column(name = "member_since")
    private LocalDate memberSince;

    @Column(name = "member_until")
    private LocalDate memberUntil;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "membership_paid", nullable = false)
    private boolean membershipPaid = false;

    @Column(length = 500)
    private String notes;

    private boolean enabled = true;

    public ClientEntity() {
    }

    public ClientEntity(Long id, String code, String dni, String fullName, String email, String phone,
                        String address, com.library.client.domain.model.ClientType type,
                        com.library.client.domain.model.ClientStatus status, LocalDate memberSince,
                        LocalDate memberUntil, LocalDate birthDate, boolean membershipPaid, String notes, boolean enabled) {
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
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public com.library.client.domain.model.ClientType getType() {
        return type;
    }

    public void setType(com.library.client.domain.model.ClientType type) {
        this.type = type;
    }

    public com.library.client.domain.model.ClientStatus getStatus() {
        return status;
    }

    public void setStatus(com.library.client.domain.model.ClientStatus status) {
        this.status = status;
    }

    public LocalDate getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(LocalDate memberSince) {
        this.memberSince = memberSince;
    }

    public LocalDate getMemberUntil() {
        return memberUntil;
    }

    public void setMemberUntil(LocalDate memberUntil) {
        this.memberUntil = memberUntil;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isMembershipPaid() {
        return membershipPaid;
    }

    public void setMembershipPaid(boolean membershipPaid) {
        this.membershipPaid = membershipPaid;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
