package com.library.iam.infrastructure.persistence.repository.jpa;

import com.library.iam.infrastructure.persistence.entity.PermissionEntity;
import com.library.kernel.jpa.CrudRepository;
import com.library.kernel.jpa.FindableByCode;

/**
 * Repository interface for {@link PermissionEntity}, composing the kernel
 * {@link PermissionRepository} capability contract and fixing the entity types.
 *
 * <p>The generic CRUD + find-by-code operations are inherited from
 * {@link PermissionRepository}. This concrete interface only fixes the generic
 * parameters to {@code PermissionEntity} / {@code Long}; the Hibernate-backed
 * implementation lives in
 * {@code repository.hibernate.HibernatePermissionRepository}.</p>
 */
public interface PermissionJpaRepository<T, ID> extends CrudRepository<T, ID>, FindableByCode<T> {
    // Types fixed to PermissionEntity / Long only.
}
