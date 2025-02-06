package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.user.FriendshipStatus;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.data.jpa.EntityManagers;
import guru.qa.niffler.data.repository.UserdataRepository;
import jakarta.persistence.EntityManager;

import java.util.Optional;
import java.util.UUID;

public class UserdataRepositoryHibernate implements UserdataRepository {

    private final EntityManager em = EntityManagers.em(CFG.userdataJdbcUrl());
    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity createUser(UserEntity user) {
        em.joinTransaction();
        em.persist(user);
        return user;
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return Optional.ofNullable(em.find(UserEntity.class, id));
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try {
            return Optional.of(em.createQuery("select u from UserEntity u where u.username =: username", UserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(UserEntity user) {
        em.joinTransaction();
        em.remove(user);
    }

    @Override
    public void sendInvitation(UserEntity requester, UserEntity addressee) {
        em.joinTransaction();
        requester.addFriends(FriendshipStatus.PENDING, addressee);
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        em.joinTransaction();
        requester.addFriends(FriendshipStatus.ACCEPTED, addressee);
        addressee.addFriends(FriendshipStatus.ACCEPTED, requester);
    }

    @Override
    public UserEntity update(UserEntity user) {
        em.joinTransaction();
        return em.merge(user);
    }
}
