package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class SpendRepositoryJdbc implements SpendRepository {

    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        Optional<CategoryEntity> category = categoryDao.findCategoryById(spend.getCategory().getId());

        if (category.isEmpty()) {
            spend.setCategory(categoryDao.create(spend.getCategory()));
        }

        return spendDao.createSpend(spend);
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        return categoryDao.create(category);
    }

    @Override
    public SpendEntity updateSpend(SpendEntity spend) {
        categoryDao.updateCategory(spend.getCategory());
        spendDao.update(spend);
        return spend;
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
}
