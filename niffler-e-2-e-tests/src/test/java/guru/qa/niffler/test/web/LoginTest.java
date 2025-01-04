package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@WebTest
public class LoginTest {
    private static final Config CFG = Config.getInstance();
    Faker faker = new Faker();

    @Test
    public void mainPageShouldBeDisplayedAfterSuccessfulLogin() {
        open(CFG.frontUrl(), LoginPage.class)
                .doLogin("vasvap", "12345")
                .checkThatMainPageHasStatistics()
                .checkThatMainPageHasHistory()
                .checkMainPageUrl(CFG.frontUrl() + "main");
    }

    @Test
    public void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        open(CFG.frontUrl(), LoginPage.class)
                .doLoginWithBadCredentials(faker.internet().username(), faker.internet().password(3, 12))
                .checkBadCredentialsErrorMsg()
                .checkLoginPageTitle();
    }
}
