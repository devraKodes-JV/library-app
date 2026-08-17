package com.library.iam.infrastructure.persistence.repository.jpa;

import com.library.iam.infrastructure.persistence.entity.RoleEntity;
import com.library.kernel.jpa.CrudRepository;
import com.library.kernel.jpa.FindableByName;

/**
 * Repository interface for {@link RoleEntity}, composing the kernel
 * {@link RoleRepository} capability contract and fixing the entity types.
 *
 * <p>The generic CRUD + find-by-name operations are inherited from
 * {@link RoleRepository}. This concrete interface only fixes the generic
 * parameters to {@code RoleEntity} / {@code Long}; the Hibernate-backed
 * implementation lives in {@code repository.hibernate.HibernateRoleRepository}.</p>
 */
public interface RoleJpaRepository<T, ID> extends CrudRepository<T, ID>, FindableByName<T> {
    // Types fixed to RoleEntity / Long only.
}
