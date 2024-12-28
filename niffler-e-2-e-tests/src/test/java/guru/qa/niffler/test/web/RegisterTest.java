package guru.qa.niffler.test.web;


import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith(BrowserExtension.class)
public class RegisterTest {

    private static final Config CFG = Config.getInstance();
    Faker faker = new Faker();
    String username = "testUser";
    String password = "test123";

    @Test
    public void shouldRegisterNewUser() {
        String userPass = faker.internet().password(3, 12);
        open(CFG.frontUrl(), LoginPage.class)
                .createAccount()
                .setUserName(faker.internet().username())
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
                .setUserName(faker.internet().username())
                .setPassword(faker.internet().password(3,12))
                .setPasswordSubmit(faker.internet().password(3,12))
                .submitRegistration()
                .checkPasswordAndConfirmPasswordAreNotEqualErrorMsg();
    }
}
