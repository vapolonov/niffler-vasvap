package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SpendDbClient {

    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    public SpendJson createSpend(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
        if(spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
        }

        return SpendJson.fromEntity(
                spendDao.create(spendEntity)
        );
    }

    public CategoryJson createCategory(CategoryJson category) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
        return CategoryJson.fromEntity(categoryDao.create(categoryEntity));
    }

    public void deleteCategory(CategoryJson category) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
        categoryDao.deleteCategory(categoryEntity);
    }

    public void deleteSpend(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
        spendDao.deleteSpend(spendEntity);
    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName).map(CategoryJson::fromEntity);
    }

    public List<CategoryJson> findAllCategoriesByUsername1(String username) {
        return categoryDao
                .findAllCategoriesByUsername(username)
                .stream()
                .map(CategoryJson::fromEntity)
                .collect(Collectors.toList());
    }

    public List<SpendJson> findAllSpendsByUsername(String username) {
        return spendDao
                .findAllSpendsByUsername(username)
                .stream()
                .map(SpendJson::fromEntity)
                .collect(Collectors.toList());
    }
}
