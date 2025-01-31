package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.*;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.UserdataRepositoryJdbc;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;

import static guru.qa.niffler.data.tpl.DataSources.dataSource;

public class UsersDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserRepository authUserRepository = new AuthUserRepositoryJdbc();
    private final UserdataRepository userdataRepository = new UserdataRepositoryJdbc();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new ChainedTransactionManager(
                    new JdbcTransactionManager(
                            dataSource(CFG.authJdbcUrl())
                    ),
                    new JdbcTransactionManager(
                            dataSource(CFG.userdataJdbcUrl())
                    )
            )
    );

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    public UserJson createUserTxTemplate(UserJson user) {
        return txTemplate.execute(txStatus -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword(pe.encode("12345"));
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
                    authUserRepository.create(authUser);
                    return UserJson.fromEntity(
                            userdataRepository.createUser(UserEntity.fromJson(user)),
                            null
                    );
                }
        );
    }

    public UserJson createUserSpringJdbc(UserJson user) {
        return xaTxTemplate.execute(() -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword(pe.encode("12345"));
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
                    authUserRepository.create(authUser);
                    return UserJson.fromEntity(
                            userdataRepository.createUser(UserEntity.fromJson(user)),
                            null
                    );
                }
        );
    }

    public UserJson createUser(UserJson user) {
        return UserJson.fromEntity(
                xaTxTemplate.execute(() -> {
                            AuthUserEntity authUser = new AuthUserEntity();
                            authUser.setUsername(user.username());
                            authUser.setPassword(pe.encode("12345"));
                            authUser.setEnabled(true);
                            authUser.setAccountNonExpired(true);
                            authUser.setAccountNonLocked(true);
                            authUser.setCredentialsNonExpired(true);
                            authUser.setAuthorities(
                                    Arrays.stream(Authority.values())
                                            .map(a -> {
                                                        AuthorityEntity ae = new AuthorityEntity();
                                                        ae.setUser(authUser);
                                                        ae.setAuthority(a);
                                                        return ae;
                                                    }
                                            ).toList()
                            );
                            authUserRepository.create(authUser);

                            UserEntity ue = new UserEntity();
                            ue.setUsername(user.username());
                            ue.setFullname(user.fullname());
                            ue.setCurrency(user.currency());
                            userdataRepository.createUser(ue);
                            return ue;
                        }
                ), null);
    }
}
