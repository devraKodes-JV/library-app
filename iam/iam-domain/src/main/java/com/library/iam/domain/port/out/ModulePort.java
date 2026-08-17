package com.library.iam.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.library.iam.domain.model.Module;

/**
 * Output (driven) port for module persistence.
 *
 * <p>Implemented by an adapter in the infrastructure layer. The domain and
 * application layers depend only on this interface. Modules are seeded in
 * code (Flyway migrations) and are NOT managed at runtime.</p>
 */
public interface ModulePort {

    /**
     * Finds a module by its code, if present.
     *
     * @param code the module code (e.g. "iam", "catalog")
     * @return an {@link Optional} containing the domain {@link Module}, or empty
     */
    Optional<Module> findByCode(String code);

    /**
     * Lists all non-deleted modules.
     *
     * @return the list of active modules (never null)
     */
    List<Module> findAll();

    /**
     * Persists a module.
     *
     * @param module the domain module to save
     * @return the saved module
     */
    Module save(Module module);
}
