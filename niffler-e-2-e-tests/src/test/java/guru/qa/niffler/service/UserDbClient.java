package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases.XaFunction;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UserdataDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.model.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import static guru.qa.niffler.data.Databases.xaTransaction;

public class UserDbClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  public UserJson createUser(UserJson user) {
    return UserJson.fromEntity(
        xaTransaction(
            new XaFunction<>(
                connection -> {
                  AuthUserEntity authUser = new AuthUserEntity();
                  authUser.setUsername(user.username());
                  authUser.setPassword(pe.encode("12345"));
                  authUser.setEnabled(true);
                  authUser.setAccountNonExpired(true);
                  authUser.setAccountNonLocked(true);
                  authUser.setCredentialsNonExpired(true);
                  new AuthUserDaoJdbc(connection).create(authUser);
                  new AuthAuthorityDaoJdbc(connection).create(
                          Arrays.stream(Authority.values())
                              .map(a -> {
                                    AuthAuthorityEntity ae = new AuthAuthorityEntity();
                                    ae.setUserId(authUser.getId());
                                    ae.setAuthority(a);
                                    return ae;
                                  }
                              ).toArray(AuthAuthorityEntity[]::new));
                  return null;
                },
                CFG.authJdbcUrl()
            ),
            new XaFunction<>(
                connection -> {
                  UserEntity ue = new UserEntity();
                  ue.setUsername(user.username());
                  ue.setFullname(user.fullname());
                  ue.setCurrency(user.currency());
                  new UserdataDaoJdbc(connection).createUser(ue);
                  return ue;
                },
                CFG.userdataJdbcUrl()
            )
        )
    );
  }
}