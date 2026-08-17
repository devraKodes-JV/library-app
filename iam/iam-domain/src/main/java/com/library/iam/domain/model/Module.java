package com.library.iam.domain.model;

/**
 * Module of the system (pure domain object, no framework dependency).
 *
 * <p>Represents a functional area/section of the application (catalog, iam,
 * reservations, reports, ...). Permissions are grouped by module and the
 * navigation side menu is built dynamically from them.</p>
 *
 * <p>Part of the pure domain layer ({@code iam-domain} module).</p>
 */
public class Module {

    private Long id;
    private String code;
    private String name;
    private String menuLabel;
    private String icon;
    private Integer sortOrder;
    private boolean enabled;

    /**
     * @param id        database identifier (null for a not-yet-persisted module)
     * @param code      unique module code (e.g. "iam", "catalog")
     * @param name      descriptive name of the module
     * @param menuLabel label of the module section in the navigation menu
     * @param icon      icon name for the module section
     * @param sortOrder vertical ordering of the module in the menu
     * @param enabled   whether the module is active
     */
    public Module(Long id, String code, String name, String menuLabel, String icon,
                  Integer sortOrder, boolean enabled) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.menuLabel = menuLabel;
        this.icon = icon;
        this.sortOrder = sortOrder;
        this.enabled = enabled;
    }

    /** Convenience factory for a new (not yet persisted) module. */
    public static Module withoutId(String code, String name, String menuLabel, String icon,
                                   Integer sortOrder, boolean enabled) {
        return new Module(null, code, name, menuLabel, icon, sortOrder, enabled);
    }

    // Getters and setters.

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
