package guru.qa.niffler.data.dao;

import java.util.List;
import java.util.UUID;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface AuthAuthorityDao {

    void create(AuthorityEntity... authority);

    @Nonnull
    List<AuthorityEntity> findAll();

    @Nonnull
    List<AuthorityEntity> findAllByUserId(UUID userId);

}
