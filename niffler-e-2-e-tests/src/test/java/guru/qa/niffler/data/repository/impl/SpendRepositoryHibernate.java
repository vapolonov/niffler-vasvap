package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jpa.EntityManagers.em;

public class SpendRepositoryHibernate implements SpendRepository {

    private static final Config CFG = Config.getInstance();
    private final EntityManager entityManager = em(CFG.spendJdbcUrl());

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        entityManager.joinTransaction();
        entityManager.persist(spend);
        return spend;
    }

    @Override
    public SpendEntity updateSpend(SpendEntity spend) {
        entityManager.joinTransaction();
        return entityManager.merge(spend);
    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
        return Optional.ofNullable(entityManager.find(SpendEntity.class, id));
    }

    @Override
    public Optional<SpendEntity> findSpendsByUsernameAndSpendDescr(String username, String spendDescr) {
        try {
            return Optional.of(entityManager.createQuery("SELECT s FROM SpendEntity s WHERE s.username =: username AND s.description =: description", SpendEntity.class)
                    .setParameter("username", username)
                    .setParameter("description", spendDescr)
                    .getSingleResult()
            );
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<SpendEntity> findAllSpends() {
        return entityManager.createQuery("SELECT s FROM SpendEntity s", SpendEntity.class).getResultList();
    }

    @Override
    public void deleteSpend(SpendEntity spend) {
        entityManager.joinTransaction();
        entityManager.remove(entityManager.contains(spend) ? spend : entityManager.merge(spend));
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        entityManager.joinTransaction();
        entityManager.persist(category);
        return category;
    }

    @Override
    public void updateCategory(CategoryEntity category) {
        entityManager.joinTransaction();
        entityManager.merge(category);
    }

    @Override
    public Optional<CategoryEntity> findCategoryByID(UUID id) {
        return Optional.ofNullable(entityManager.find(CategoryEntity.class, id));

    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        try {
            return Optional.of(
                    entityManager.createQuery("SELECT c FROM CategoryEntity c WHERE c.username =: username and c.name =: name", CategoryEntity.class)
                            .setParameter("username", username)
                            .setParameter("name", categoryName)
                            .getSingleResult()
            );
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<CategoryEntity> findAllCategoriesByUsername(String username) {
        return entityManager.createQuery("SELECT c FROM CategoryEntity c WHERE c.username =: username", CategoryEntity.class).getResultList();
    }

    @Override
    public List<CategoryEntity> findAllCategories() {
        return entityManager.createQuery("SELECT c FROM CategoryEntity c", CategoryEntity.class).getResultList();
    }

    @Override
    public void deleteCategory(CategoryEntity category) {
        entityManager.joinTransaction();
        entityManager.remove(entityManager.contains(category) ? category : entityManager.merge(category));
    }
}
