package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

@WebTest
public class ProfileTest {

    private static final Config CFG = Config.getInstance();

    @User(
            username = "vasvap",
            categories = {@Category(archived = true)}
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson[] category) {
        open(CFG.frontUrl(), LoginPage.class)
                .doLogin("vasvap", "12345")
                .selectUserProfile()
                .selectCheckbox()
                .checkCategoryInList(category[0].name());
    }

    @User(
            username = "vasvap",
            categories = {@Category(archived = false)}
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        open(CFG.frontUrl(), LoginPage.class)
                .doLogin("vasvap", "12345")
                .selectUserProfile()
                .checkCategoryInList(category.name());
    }

    @User(
            username = "vasvap",
            categories = {@Category(archived = false)}
    )
    @Test
    void activeCategoryShouldPresentInCategoriesListV2(CategoryJson category) {
        open(CFG.frontUrl(), LoginPage.class)
                .doLogin("vasvap", "12345")
                .checkThatMainPageHasHistory();

        open(CFG.frontUrl() + "profile", ProfilePage.class)
                .checkCategoryInList(category.name());
    }
}
