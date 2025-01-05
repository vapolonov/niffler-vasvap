package guru.qa.niffler.test.web;


import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.test.web.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.test.web.utils.RandomDataUtils.randomUsername;

@WebTest
public class RegisterTest {

    private static final Config CFG = Config.getInstance();

    String username = "testUser";
    String password = "test123";

    @Test
    public void shouldRegisterNewUser() {
        String userPass = randomPassword();
        open(CFG.frontUrl(), LoginPage.class)
                .createAccount()
                .setUserName(randomUsername())
                .setPassword(userPass)
                .setPasswordSubmit(userPass)
                .submitRegistration()
                .successRegistration()
                .doLogin(username, password)
                .checkThatMainPageHasStatistics()
                .checkThatMainPageHasHistory();
    }

    @Test
    public void shouldNotRegisterUserWithExistingUsername() {
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
        open(CFG.frontUrl(), LoginPage.class)
                .createAccount()
                .setUserName(randomUsername())
                .setPassword(randomPassword())
                .setPasswordSubmit(randomPassword())
                .submitRegistration()
                .checkPasswordAndConfirmPasswordAreNotEqualErrorMsg();
    }
}
