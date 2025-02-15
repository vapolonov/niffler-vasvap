package guru.qa.niffler.data.dao;

import java.util.List;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

public interface AuthAuthorityDao {

    void create(AuthorityEntity... authority);

    List<AuthorityEntity> update(AuthorityEntity... authority);

    List<AuthorityEntity> findAll();

    void remove(AuthorityEntity... authority);

}
