package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.test.web.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.test.web.utils.RandomDataUtils.randomUsername;

@WebTest
public class LoginTest {
    private static final Config CFG = Config.getInstance();

    @User(
            categories = {
                    @Category(name = "Магазины", archived = true),
                    @Category(name = "Бары", archived = false)
            },
            spendings = {
                    @Spending(
                            category = "Обучение",
                            description = "QA.GURU Advanced",
                            amount = 80000
                    )
            }
    )
    @Test
    public void mainPageShouldBeDisplayedAfterSuccessfulLogin(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .checkThatMainPageHasStatistics()
                .checkThatMainPageHasHistory()
                .checkMainPageUrl(CFG.frontUrl() + "main");
    }

    @Test
    public void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        open(CFG.frontUrl(), LoginPage.class)
                .doLoginWithBadCredentials(randomUsername(), randomPassword())
                .checkBadCredentialsErrorMsg()
                .checkLoginPageTitle();
    }
}
