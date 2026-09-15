package com.library.config.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Entity
@Table(name = "settings")
@Audited
public class SettingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "module", nullable = false, length = 50)
    private String module;

    @Column(name = "setting_key", nullable = false, length = 100)
    private String key;

    @Column(name = "setting_value", nullable = false, columnDefinition = "TEXT")
    private String value;

    @Column(name = "setting_type", nullable = false, length = 20)
    private String type;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    public SettingEntity() {}

    public SettingEntity(Long id, String module, String key, String value, String type, String description, LocalDateTime updatedAt, String updatedBy) {
        this.id = id;
        this.module = module;
        this.key = key;
        this.value = value;
        this.type = type;
        this.description = description;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    public Long getId() { return id; }
    public String getModule() { return module; }
    public String getKey() { return key; }
    public String getValue() { return value; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getUpdatedBy() { return updatedBy; }

    public void setId(Long id) { this.id = id; }
    public void setModule(String module) { this.module = module; }
    public void setKey(String key) { this.key = key; }
    public void setValue(String value) { this.value = value; }
    public void setType(String type) { this.type = type; }
    public void setDescription(String description) { this.description = description; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
}
