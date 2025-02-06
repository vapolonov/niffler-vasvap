package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.SpendRepositoryJdbc;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    private final SpendRepository spendRepository = new SpendRepositoryJdbc();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    public SpendJson createSpend(SpendJson spend) {
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = spendRepository
                                .createCategory(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            spendRepository.createSpend(spendEntity)
                    );
                }
        );
    }

    public CategoryJson createCategory(CategoryJson category) {
        return jdbcTxTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    return CategoryJson.fromEntity(spendRepository.createCategory(categoryEntity));
                }
        );
    }

    public void deleteCategory(CategoryJson category) {
        jdbcTxTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    spendRepository.deleteCategory(categoryEntity);
                    return null;
                }
        );
    }

    public void deleteSpend(SpendJson spend) {
        jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    spendRepository.deleteSpend(spendEntity);
                    return null;
                }
        );
    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return jdbcTxTemplate.execute(() -> spendRepository
                .findCategoryByUsernameAndCategoryName(username, categoryName)
                .map(CategoryJson::fromEntity)
        );
    }

    public List<CategoryJson> findAllCategoriesByUsername(String username) {
        return jdbcTxTemplate.execute(() -> spendRepository
            .findAllCategoriesByUsername(username)
            .stream()
            .map(CategoryJson::fromEntity)
            .collect(Collectors.toList())
        );
    }

    public List<SpendJson> findSpendsByUsernameAndSpendDescr(String username, String spendDescr) {
        return jdbcTxTemplate.execute(() -> spendRepository
            .findSpendsByUsernameAndSpendDescr(username, spendDescr)
            .stream()
            .map(SpendJson::fromEntity)
            .collect(Collectors.toList())
        );
    }
}
