package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendRepository {

    CategoryEntity createCategory(CategoryEntity category);

    void updateCategory(CategoryEntity category);

    Optional<CategoryEntity> findCategoryByID(UUID id);

    Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName);

    List<CategoryEntity> findAllCategoriesByUsername(String username);

    List<CategoryEntity> findAllCategories();

    void deleteCategory(CategoryEntity category);

    SpendEntity createSpend(SpendEntity spend);

    SpendEntity updateSpend(SpendEntity spend);

    Optional<SpendEntity> findSpendById(UUID id);

    Optional<SpendEntity> findSpendsByUsernameAndSpendDescr(String username, String spendDescr);

    List<SpendEntity> findAllSpends();

    void deleteSpend(SpendEntity spend);
}
