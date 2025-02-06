package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.jpa.EntityManagers;
import guru.qa.niffler.data.repository.AuthUserRepository;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthUserRepositoryHibernate implements AuthUserRepository {

    private final EntityManager em = EntityManagers.em(CFG.authJdbcUrl());
    private static final Config CFG = Config.getInstance();

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        em.joinTransaction();
        em.persist(user);
        return user;
    }

    @Override
    public AuthUserEntity update(AuthUserEntity user) {
        em.joinTransaction();
        em.merge(user);
        return user;
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        return Optional.ofNullable(em.find(AuthUserEntity.class, id));
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        try {
            return Optional.of(em.createQuery("select u from UserEntity u where u.username =: username", AuthUserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void remove(AuthUserEntity user) {
        em.joinTransaction();
        em.remove(user);
    }

    @Override
    public List<AuthUserEntity> findAll() {
        return em.createQuery("SELECT u FROM AuthUserEntity u", AuthUserEntity.class).getResultList();
    }
}
