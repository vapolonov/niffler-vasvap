package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.test.web.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.test.web.utils.RandomDataUtils.randomUsername;

@WebTest
public class LoginTest {
    private static final Config CFG = Config.getInstance();

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
                .doLoginWithBadCredentials(randomUsername(), randomPassword())
                .checkBadCredentialsErrorMsg()
                .checkLoginPageTitle();
    }
}
