package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataRepository;
import guru.qa.niffler.data.repository.impl.*;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import static guru.qa.niffler.test.web.utils.RandomDataUtils.randomUsername;

public class UsersDbClient implements UsersClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

//    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
//    private final UserdataRepository userdataRepository = new UserdataRepositoryHibernate();
    private final AuthUserRepository authUserRepository = new AuthUserRepositorySpringJdbc();
    private final UserdataRepository userdataRepository = new UserdataRepositorySpringJdbc();
//    private final AuthUserRepository authUserRepository = new AuthUserRepositoryJdbc();
//    private final UserdataRepository userdataRepository = new UserdataRepositoryJdbc();

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    @NotNull
    @Override
    public UserJson createUser(String username, String password) {
        return xaTxTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntity(username, password);
                    authUserRepository.create(authUser);
                    return UserJson.fromEntity(
                            userdataRepository.createUser(userEntity(username)),
                            null
                    );
                }
        );
    }

    @Override
    public void createIncomeInvitations(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = userdataRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                    final String username = randomUsername();
                    UserEntity addressee = createNewUser(username);
                    userdataRepository.sendInvitation(addressee, targetEntity);
                    return null;
                });
            }
        }
    }


    @Override
    public void createOutcomeInvitations(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = userdataRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                    final String username = randomUsername();
                    UserEntity addressee = createNewUser(username);

                    userdataRepository.sendInvitation(targetEntity, addressee);
                    return null;
                });
            }
        }

    }

    @Override
    public void createFriends(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = userdataRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                            final String username = randomUsername();
                            UserEntity addressee = createNewUser(username);
                            userdataRepository.addFriend(
                                    targetEntity,
                                    addressee
                            );
                            return null;
                        }
                );
            }
        }
    }

    public void deleteUser(UserJson user) {
        xaTxTemplate.execute( () -> {
            UserEntity ueToDelete = userdataRepository.findById(
                    user.id()
            ).orElseThrow();
            AuthUserEntity aueToDelete = authUserRepository.findByUsername(
                    user.username()
            ).orElseThrow();

            authUserRepository.remove(aueToDelete);
            userdataRepository.delete(ueToDelete);
            return null;
        });
    }

    private AuthUserEntity authUserEntity(String username, String password) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword(pe.encode(password));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(
                Arrays.stream(Authority.values()).map(
                        e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setUser(authUser);
                            ae.setAuthority(e);
                            return ae;
                        }
                ).toList()
        );
        return authUser;
    }

    private UserEntity userEntity(String username) {
        UserEntity ue = new UserEntity();
        ue.setUsername(username);
        ue.setCurrency(CurrencyValues.RUB);
        return ue;
    }

    private UserEntity createNewUser(String username) {
        AuthUserEntity authUser = authUserEntity(username, "12345");
        authUserRepository.create(authUser);
        return userdataRepository.createUser(userEntity(username));
    }
}

