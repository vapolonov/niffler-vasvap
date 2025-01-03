package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith(BrowserExtension.class)
public class ProfileTest {

    private static final Config CFG = Config.getInstance();

    @Category(
            username = "vasvap",
            archived = true
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        open(CFG.frontUrl(), LoginPage.class)
                .doLogin("vasvap", "12345")
                .selectUserProfile()
                .selectCheckbox()
                .checkCategoryInList(category.name());
    }

    @Category(
            username = "vasvap",
            archived = false  // можно убрать т.к. по дефолту false
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        open(CFG.frontUrl(), LoginPage.class)
                .doLogin("vasvap", "12345")
                .selectUserProfile()
                .checkCategoryInList(category.name());
    }

    @Category(
            username = "vasvap",
            archived = false  // можно убрать т.к. по дефолту false
    )
    @Test
    void activeCategoryShouldPresentInCategoriesListV2(CategoryJson category) {
        open(CFG.frontUrl(), LoginPage.class)
                .doLogin("vasvap", "12345");

        open(CFG.frontUrl() + "profile", ProfilePage.class)
                .checkCategoryInList(category.name());
    }


}
