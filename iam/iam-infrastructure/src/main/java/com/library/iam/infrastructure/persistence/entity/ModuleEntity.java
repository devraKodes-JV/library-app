package com.library.iam.infrastructure.persistence.entity;

import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity for a module (persistence adapter).
 *
 * <p>The pure domain model is {@code com.library.iam.domain.model.Module}.
 * The {@code ModuleMapper} converts between the two.</p>
 *
 * <p>Extends {@link AuditableEntity} to gain audit columns and logical delete.</p>
 */
@Entity
@Table(name = "modules")
@Audited
public class ModuleEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "menu_label", length = 150)
    private String menuLabel;

    @Column(length = 100)
    private String icon;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @Column(nullable = false)
    private boolean enabled = true;

    protected ModuleEntity() {
        // Required by JPA.
    }

    public ModuleEntity(Long id, String code, String name, String menuLabel,
                        String icon, Integer sortOrder, boolean enabled) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.menuLabel = menuLabel;
        this.icon = icon;
        this.sortOrder = sortOrder;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenuLabel() {
        return menuLabel;
    }

    public void setMenuLabel(String menuLabel) {
        this.menuLabel = menuLabel;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
