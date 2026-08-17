package com.library.iam.application.dto;

/**
 * Individual item of the side navigation menu (aside) - output DTO.
 *
 * <p>This is an immutable value object (Java record) produced by the
 * application layer and consumed by the web/template layer. It decouples the
 * template from the domain model: the template only sees the data it needs to
 * render the menu.</p>
 *
 * @param permissionId id of the underlying permission
 * @param code         permission code (e.g. "books.read")
 * @param label        label shown in the menu
 * @param icon         icon of the item
 * @param url          URL the item navigates to
 * @param sortOrder    ordering within the module
 */
public record NavItem(
        Long permissionId,
        String code,
        String label,
        String icon,
        String url,
        Integer sortOrder) {
}
