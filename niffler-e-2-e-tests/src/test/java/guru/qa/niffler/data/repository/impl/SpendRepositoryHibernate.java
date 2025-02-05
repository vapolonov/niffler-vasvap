package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.jpa.EntityManagers;
import guru.qa.niffler.data.repository.SpendRepository;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendRepositoryHibernate implements SpendRepository {

    private static final Config CFG = Config.getInstance();
    private final EntityManager em = EntityManagers.em(CFG.spendJdbcUrl());

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        em.joinTransaction();
        em.persist(spend);
        return spend;
    }

    @Override
    public SpendEntity updateSpend(SpendEntity spend) {
        em.joinTransaction();
        em.merge(spend);
        return spend;
    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
        return Optional.ofNullable(em.find(SpendEntity.class, id));
    }

    @Override
    public Optional<SpendEntity> findSpendsByUsernameAndSpendDescr(String username, String spendDescr) {
        try {
            return Optional.of(em.createQuery("SELECT s FROM SpendEntity s WHERE s.username =: username AND s.description =: description", SpendEntity.class)
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
        return em.createQuery("SELECT s FROM SpendEntity s", SpendEntity.class).getResultList();
    }

    @Override
    public void deleteSpend(SpendEntity spend) {
        em.joinTransaction();
        em.remove(spend);
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        em.joinTransaction();
        em.persist(category);
        return category;
    }

    @Override
    public void updateCategory(CategoryEntity category) {
        em.joinTransaction();
        em.merge(category);
    }

    @Override
    public Optional<CategoryEntity> findCategoryByID(UUID id) {
        return Optional.ofNullable(em.find(CategoryEntity.class, id));

    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        try {
            return Optional.of(em.createQuery("SELECT c FROM CategoryEntity c WHERE c.username =: username and c.name =: name", CategoryEntity.class)
                    .setParameter("username", username)
                    .setParameter("name", categoryName)
                    .getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<CategoryEntity> findAllCategoriesByUsername(String username) {
        return em.createQuery("SELECT c FROM CategoryEntity c WHERE c.username =: username", CategoryEntity.class).getResultList();
    }

    @Override
    public List<CategoryEntity> findAllCategories() {
        return em.createQuery("SELECT c FROM CategoryEntity c", CategoryEntity.class).getResultList();
    }

    @Override
    public void deleteCategory(CategoryEntity category) {
        em.joinTransaction();
        em.remove(category);
    }
}
