package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.data.dao.UserdataDao;
import guru.qa.niffler.data.dao.impl.UserdataDaoSpringJdbc;
import guru.qa.niffler.data.entity.user.FriendshipStatus;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.data.repository.UserdataRepository;

import java.util.Optional;
import java.util.UUID;

public class UserdataRepositorySpringJdbc implements UserdataRepository {

    private final UserdataDao userdataDao = new UserdataDaoSpringJdbc();

    @Override
    public UserEntity createUser(UserEntity user) {
        return userdataDao.createUser(user);
    }

    @Override
    public UserEntity update(UserEntity user) {
        return userdataDao.createUser(user);
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return userdataDao.findById(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userdataDao.findByUsername(username);
    }

    @Override
    public void delete(UserEntity user) {
        userdataDao.delete(user);
    }

    @Override
    public void sendInvitation(UserEntity requester, UserEntity addressee) {
        requester.addFriends(FriendshipStatus.PENDING, addressee);
        userdataDao.update(requester);
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        requester.addFriends(FriendshipStatus.ACCEPTED, addressee);
        addressee.addFriends(FriendshipStatus.ACCEPTED, requester);
        userdataDao.update(requester);
        userdataDao.update(addressee);
    }
}
