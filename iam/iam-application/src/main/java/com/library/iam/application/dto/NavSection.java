package com.library.iam.application.dto;

import java.util.List;

import com.library.iam.domain.model.Module;

/**
 * Section of the side navigation menu (aside) - output DTO.
 *
 * <p>Represents a module together with its menu options (the permissions that
 * have a URL and a menu label). Produced by the application layer and consumed
 * by the template layer to render the menu.</p>
 *
 * @param moduleId   id of the module
 * @param moduleCode code of the module (catalog, iam, ...)
 * @param menuLabel  title of the section in the aside
 * @param icon       icon of the module
 * @param sortOrder  vertical ordering of the module
 * @param items      menu options (navigable permissions)
 */
public record NavSection(
        Long moduleId,
        String moduleCode,
        String menuLabel,
        String icon,
        Integer sortOrder,
        List<NavItem> items) {

    /**
     * Builds a {@link NavSection} from a domain {@link Module} and its items.
     *
     * @param module the domain module (source of the section metadata)
     * @param items  the already-converted menu items
     * @return an immutable navigation section DTO
     */
    public static NavSection of(Module module, List<NavItem> items) {
        return new NavSection(
                module.getId(),
                module.getCode(),
                module.getMenuLabel() != null ? module.getMenuLabel() : module.getName(),
                module.getIcon(),
                module.getSortOrder(),
                items);
    }
}
