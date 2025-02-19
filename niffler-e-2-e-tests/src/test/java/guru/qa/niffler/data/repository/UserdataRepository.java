package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.data.repository.impl.UserdataRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.UserdataRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.UserdataRepositorySpringJdbc;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface UserdataRepository {

  @Nonnull
  static UserdataRepository getInstance() {
    return switch (System.getProperty("repository.impl", "jpa")) {
      case "jpa" -> new UserdataRepositoryHibernate();
      case "jdbc" -> new UserdataRepositoryJdbc();
      case "sjdbc" -> new UserdataRepositorySpringJdbc();
      default -> throw new IllegalStateException("Unexpected value: " + System.getProperty("repository.impl"));
    };
  }

  @Nonnull
  UserEntity create(UserEntity user);

  @Nonnull
  UserEntity update(UserEntity user);

  @Nonnull
  Optional<UserEntity> findById(UUID id);

  @Nonnull
  Optional<UserEntity> findByUsername(String username);

  void addFriendshipRequest(UserEntity requester, UserEntity addressee);

  void addFriend(UserEntity requester, UserEntity addressee);
}
