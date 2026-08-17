package com.library.iam.domain.model;

/**
 * Granular permission of the system (pure domain object, no framework
 * dependency).
 *
 * <p>A permission uses the {@code resource.action} notation, for example
 * {@code books.read}, {@code books.write}, {@code users.manage}. Each
 * permission belongs to a module (catalog, iam, ...), which allows the
 * navigation side menu to be built dynamically from the database.</p>
 *
 * <p>Part of the pure domain layer ({@code iam-domain} module).</p>
 */
public class Permission {

    private Long id;
    private String code;
    private String name;
    private String menuLabel;
    private String icon;
    private String url;
    private Integer sortOrder;
    private boolean enabled = true;
    private Module module;

    /**
     * @param id        database identifier (null for a not-yet-persisted permission)
     * @param code      unique permission code (e.g. "books.read")
     * @param name      descriptive name of the permission
     * @param menuLabel label shown in the navigation menu (null if not navigable)
     * @param icon      icon name for the menu item
     * @param url       URL the menu item navigates to (null if not navigable)
     * @param sortOrder ordering within the module's menu
     * @param module    the module this permission belongs to
     */
    public Permission(Long id, String code, String name, String menuLabel, String icon,
                      String url, Integer sortOrder, Module module) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.menuLabel = menuLabel;
        this.icon = icon;
        this.url = url;
        this.sortOrder = sortOrder;
        this.module = module;
        this.enabled = true;
    }

    /**
     * Convenience factory for a new (not yet persisted, enabled) permission.
     *
     * @param module the module this permission belongs to (may be null)
     */
    public static Permission withoutId(String code, String name, String menuLabel,
                                       String icon, String url, Integer sortOrder,
                                       Module module) {
        return new Permission(null, code, name, menuLabel, icon, url, sortOrder, module);
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}
