package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendRepositorySpringJdbc implements SpendRepository {

    private final SpendDao spendDao = new SpendDaoSpringJdbc();
    private final CategoryDao categoryDao = new CategoryDaoSpringJdbc();


    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        return categoryDao.create(category);
    }

    @Override
    public void updateCategory(CategoryEntity category) {
        categoryDao.updateCategory(category);

    }

    @Override
    public Optional<CategoryEntity> findCategoryByID(UUID id) {
        return categoryDao.findCategoryById(id);
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName);
    }

    @Override
    public List<CategoryEntity> findAllCategoriesByUsername(String username) {
        return categoryDao.findAllCategoriesByUsername(username);
    }

    @Override
    public List<CategoryEntity> findAllCategories() {
        return categoryDao.findAllCategories();
    }

    @Override
    public void deleteCategory(CategoryEntity category) {
        categoryDao.deleteCategory(category);
    }

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        Optional<CategoryEntity> category = categoryDao.findCategoryById(spend.getCategory().getId());

        if (category.isEmpty()) {
            spend.setCategory(categoryDao.create(spend.getCategory()));
        }

        return spendDao.createSpend(spend);
    }

    @Override
    public SpendEntity updateSpend(SpendEntity spend) {
        return spendDao.update(spend);
    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
        return spendDao.findSpendById(id);
    }

    @Override
    public Optional<SpendEntity> findSpendsByUsernameAndSpendDescr(String username, String spendDescr) {
        return spendDao.findByUsernameAndSpendDescription(username, spendDescr);
    }

    @Override
    public List<SpendEntity> findAllSpends() {
        return spendDao.findAllSpends();
    }

    @Override
    public void deleteSpend(SpendEntity spend) {
        spendDao.deleteSpend(spend);
    }
}
