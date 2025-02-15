package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendDao {
    SpendEntity createSpend(SpendEntity spend);

    SpendEntity update(SpendEntity spend);

    Optional<SpendEntity> findSpendById(UUID id);

    Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description);

    List<SpendEntity> findAllSpends();

    void deleteSpend(SpendEntity spend);

    List<SpendEntity> findAllSpendsByUsername(String username);
}
