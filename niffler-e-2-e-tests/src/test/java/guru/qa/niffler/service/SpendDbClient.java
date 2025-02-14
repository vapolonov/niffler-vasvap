package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.SpendRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.SpendRepositorySpringJdbc;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.Optional;


public class SpendDbClient implements SpendClient {

    private static final Config CFG = Config.getInstance();

    private final SpendRepository spendRepository = new SpendRepositorySpringJdbc();
//    private final SpendRepository spendRepository = new SpendRepositoryJdbc();
//    private final SpendRepository spendRepository = new SpendRepositoryHibernate();

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    public SpendJson createSpend(SpendJson spend) {
        return xaTxTemplate.execute(() -> SpendJson.fromEntity(
                        spendRepository.createSpend(SpendEntity.fromJson(spend))
                )
        );
    }

    public CategoryJson createCategory(CategoryJson category) {
        return xaTxTemplate.execute(() -> CategoryJson.fromEntity(
                        spendRepository.createCategory(CategoryEntity.fromJson(category))
                )
        );
    }

    @Override
    public SpendJson updateSpend(SpendJson spend) {
        spendRepository.findSpendById(spend.id()).orElseThrow();
        return SpendJson.fromEntity(
                spendRepository.updateSpend(SpendEntity.fromJson(spend))
        );
    }

    @Override
    public void removeCategory(CategoryJson category) {
        xaTxTemplate.execute(() -> {
                    spendRepository.findCategoryByID(category.id());
                    spendRepository.deleteCategory(CategoryEntity.fromJson(category));
                    return null;
                }
        );
    }

    @Override
    public void removeSpend(SpendJson spend) {
        xaTxTemplate.execute(() -> {
                    spendRepository.findSpendById(spend.id());
                    spendRepository.deleteSpend(SpendEntity.fromJson(spend));
                    return null;
                }
        );
    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        Optional<CategoryEntity> cat = spendRepository.findCategoryByUsernameAndCategoryName(username, categoryName);
        return cat.map(CategoryJson::fromEntity);
    }

    public Optional<CategoryJson> findByUsernameAndSpendDescription(String username, String description) {
        Optional<CategoryEntity> cat = spendRepository.findCategoryByUsernameAndCategoryName(username, description);
        return cat.map(CategoryJson::fromEntity);
    }


}
