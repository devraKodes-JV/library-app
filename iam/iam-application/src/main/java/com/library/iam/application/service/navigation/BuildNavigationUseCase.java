package com.library.iam.application.service.navigation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.library.iam.application.dto.NavItem;
import com.library.iam.application.dto.NavSection;
import com.library.iam.domain.model.Permission;
import com.library.iam.domain.model.User;

public class BuildNavigationUseCase {

    public List<NavSection> execute(User user) {
        Map<Long, List<Permission>> byModule = new LinkedHashMap<>();

        user.getRole().getPermissions().stream()
                .filter(p -> p.getUrl() != null && !p.getUrl().isBlank())
                .forEach(p -> byModule.computeIfAbsent(
                        p.getModule().getId(), k -> new ArrayList<>()).add(p));

        return byModule.values().stream()
                .map(items -> {
                    Permission first = items.get(0);
                    List<NavItem> navItems = items.stream()
                            .sorted(Comparator.comparing(Permission::getSortOrder))
                            .map(p -> new NavItem(
                                    p.getId(),
                                    p.getCode(),
                                    p.getMenuLabel() != null ? p.getMenuLabel() : p.getName(),
                                    p.getIcon(),
                                    p.getUrl(),
                                    p.getSortOrder()))
                            .toList();
                    return NavSection.of(first.getModule(), navItems);
                })
                .sorted(Comparator.comparing(NavSection::sortOrder))
                .toList();
    }
}
