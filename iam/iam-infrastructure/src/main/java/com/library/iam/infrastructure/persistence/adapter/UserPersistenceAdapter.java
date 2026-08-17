package com.library.iam.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import com.library.iam.domain.model.User;
import com.library.iam.domain.port.out.UserPort;
import com.library.iam.infrastructure.persistence.entity.ModuleEntity;
import com.library.iam.infrastructure.persistence.entity.UserEntity;
import com.library.iam.infrastructure.persistence.mapper.UserMapper;
import com.library.iam.infrastructure.persistence.repository.jpa.UserJpaRepository;

/**
 * Persistence adapter that implements the domain {@link UserPort}.
 *
 * <p>This is the hexagonal-architecture "driven adapter". It receives domain
 * {@link User} objects, converts them to JPA entities via {@link UserMapper},
 * delegates to {@link UserJpaRepository} (Hibernate), and converts the result
 * back to the domain. The domain never sees JPA or Hibernate.</p>
 */
public class UserPersistenceAdapter implements UserPort {

    private final UserJpaRepository<UserEntity, Long> userJpaRepository;

    public UserPersistenceAdapter(UserJpaRepository<UserEntity, Long> userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username)
                .map(UserMapper::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream()
                .map(UserMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id)
                .map(UserMapper::toDomain);
    }

    @Override
    public void delete(Long id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public List<User> findInactive() {
        return userJpaRepository.findInactive().stream()
                .map(UserMapper::toDomain)
                .toList();
    }

    @Override
    public void reinstate(Long id) {
        userJpaRepository.reinstate(id);
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        UserEntity saved = userJpaRepository.save(entity);
        return UserMapper.toDomain(saved);
    }

    @Override
    public boolean updatePassword(String username, String passwordHash) {
        return userJpaRepository.updatePassword(username, passwordHash);
    }
}
