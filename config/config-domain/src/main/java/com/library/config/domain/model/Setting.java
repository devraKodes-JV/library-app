package com.library.config.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Setting {

    private Long id;
    private String module;
    private String key;
    private String value;
    private String type;
    private String description;
    private LocalDateTime updatedAt;
    private String updatedBy;

    public Setting(Long id, String module, String key, String value, String type, String description, LocalDateTime updatedAt, String updatedBy) {
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
