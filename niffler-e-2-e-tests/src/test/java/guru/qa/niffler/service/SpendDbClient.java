package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;

import guru.qa.niffler.data.dao.impl.*;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    public SpendJson createSpend(SpendJson spend) {
        return transaction(connection -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            if (spendEntity.getCategory().getId() == null) {
                CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                        .create(spendEntity.getCategory());
                spendEntity.setCategory(categoryEntity);
            }
            return SpendJson.fromEntity(
                    new SpendDaoJdbc(connection)
                            .create(spendEntity)
            );
        },
            CFG.spendJdbcUrl()
        );
    }

    public CategoryJson createCategory(CategoryJson category) {
        return transaction(connection -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            return CategoryJson.fromEntity(new CategoryDaoJdbc(connection).create(categoryEntity));
        },
            CFG.spendJdbcUrl()
        );
    }

    public void deleteCategory(CategoryJson category) {
        transaction(connection -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            new CategoryDaoJdbc(connection).deleteCategory(categoryEntity);
        },
            CFG.spendJdbcUrl()
        );
    }

    public void deleteSpend(SpendJson spend) {
        transaction(connection -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            new SpendDaoJdbc(connection).deleteSpend(spendEntity);
        },
            CFG.spendJdbcUrl()
        );
    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return transaction(connection -> {
            return new CategoryDaoJdbc(connection)
                    .findCategoryByUsernameAndCategoryName(username, categoryName)
                    .map(CategoryJson::fromEntity);
        },
            CFG.spendJdbcUrl()
        );
    }

    public List<CategoryJson> findAllCategoriesByUsername(String username) {
        return transaction(connection -> {
            return new CategoryDaoJdbc(connection)
                .findAllCategoriesByUsername(username)
                .stream()
                .map(CategoryJson::fromEntity)
                .collect(Collectors.toList());
        },
            CFG.spendJdbcUrl()
        );
    }

    public List<SpendJson> findAllSpendsByUsername(String username) {
        return transaction(connection -> {
            return new SpendDaoJdbc(connection)
                .findAllSpendsByUsername(username)
                .stream()
                .map(SpendJson::fromEntity)
                .collect(Collectors.toList());
        },
            CFG.spendJdbcUrl()
        );
    }
}
