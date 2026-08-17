package com.library.iam.infrastructure.persistence.repository.jpa;

import com.library.iam.infrastructure.persistence.entity.ModuleEntity;
import com.library.kernel.jpa.CrudRepository;
import com.library.kernel.jpa.FindableByCode;

/**
 * Repository interface for {@link ModuleEntity}, composing the kernel
 * {@link ModuleRepository} capability contract and fixing the entity types.
 *
 * <p>The generic CRUD + find-by-code operations are inherited from
 * {@link ModuleRepository}. This concrete interface only fixes the generic
 * parameters to {@code ModuleEntity} / {@code Long}; the Hibernate-backed
 * implementation lives in {@code repository.hibernate.HibernateModuleRepository}.</p>
 */
public interface ModuleJpaRepository<T, ID> extends CrudRepository<T, ID>, FindableByCode<T> {
    // Types fixed to ModuleEntity / Long only.
}
