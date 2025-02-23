package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.test.web.utils.RandomDataUtils.randomUsername;

@WebTest
public class RegisterTest {

    private static final Config CFG = Config.getInstance();

    @Test
    public void shouldRegisterNewUser() {
        String username = randomUsername();
        String password = "12345";
        open(CFG.frontUrl(), LoginPage.class)
                .createAccount()
                .setUserName(randomUsername())
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .successRegistration()
                .doLogin(username, password)
                .checkThatMainPageHasStatistics()
                .checkThatMainPageHasHistory();
    }

    @Test
    public void shouldNotRegisterUserWithExistingUsername() {
        String username = "vasvap";
        String password = "12345";
        open(CFG.frontUrl(), LoginPage.class)
                .createAccount()
                .setUserName(username)
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkUserExistsErrorMsg(username);
    }

    @Test
    public void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        String username = randomUsername();
        String password = "12345";
        open(CFG.frontUrl(), LoginPage.class)
                .createAccount()
                .setUserName(username)
                .setPassword(password)
                .setPasswordSubmit("password")
                .submitRegistration()
                .checkPasswordAndConfirmPasswordAreNotEqualErrorMsg();
    }
}
