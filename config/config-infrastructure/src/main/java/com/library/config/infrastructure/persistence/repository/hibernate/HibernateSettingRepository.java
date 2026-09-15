package com.library.config.infrastructure.persistence.repository.hibernate;

import com.library.config.domain.model.Setting;
import com.library.config.domain.repository.SettingRepository;
import com.library.config.infrastructure.persistence.entity.SettingEntity;
import com.library.config.infrastructure.persistence.mapper.SettingMapper;
import com.library.config.infrastructure.persistence.entity.SettingEntity;
import com.library.config.infrastructure.persistence.mapper.SettingMapper;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class HibernateSettingRepository implements SettingRepository {

    private final SessionFactory sessionFactory;

    public HibernateSettingRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Setting save(Setting setting) {
        try (var s = sessionFactory.openSession()) {
            var tx = s.beginTransaction();
            SettingEntity entity = SettingMapper.toEntity(setting);
            entity = s.merge(entity);
            tx.commit();
            return SettingMapper.toDomain(entity);
        }
    }

    @Override
    public Optional<Setting> findById(Long id) {
        try (var s = sessionFactory.openSession()) {
            SettingEntity entity = s.get(SettingEntity.class, id);
            return Optional.ofNullable(entity).map(SettingMapper::toDomain);
        }
    }

    @Override
    public List<Setting> findAll() {
        try (var s = sessionFactory.openSession()) {
            return s.createQuery("from SettingEntity", SettingEntity.class).list()
                    .stream().map(SettingMapper::toDomain).toList();
        }
    }

    @Override
    public List<Setting> findByModule(String module) {
        try (var s = sessionFactory.openSession()) {
            return s.createQuery("from SettingEntity where module = :module", SettingEntity.class)
                    .setParameter("module", module)
                    .list()
                    .stream().map(SettingMapper::toDomain).toList();
        }
    }

    @Override
    public Optional<Setting> findByModuleAndKey(String module, String key) {
        try (var s = sessionFactory.openSession()) {
            SettingEntity entity = s.createQuery(
                            "from SettingEntity where module = :module and key = :key", SettingEntity.class)
                    .setParameter("module", module)
                    .setParameter("key", key)
                    .uniqueResult();
            return Optional.ofNullable(entity).map(SettingMapper::toDomain);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (var s = sessionFactory.openSession()) {
            var tx = s.beginTransaction();
            SettingEntity entity = s.get(SettingEntity.class, id);
            if (entity != null) s.remove(entity);
            tx.commit();
        }
    }
}
