package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement
            usernameInput = $("input[name='username']"),
            passwordInput = $("input[name='password']"),
            submitButton = $("button[type='submit']"),
            createAccountButton = $(".form__register"),
            errorMessage = $(".form__error"),
            header = $(".header");

    public MainPage doLogin(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
        return new MainPage();
    }

    public LoginPage doLoginWithBadCredentials(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
        return this;
    }

    public RegisterPage createAccount() {
        createAccountButton.click();
        return new RegisterPage();
    }

    public LoginPage checkBadCredentialsErrorMsg() {
        errorMessage.shouldBe(visible).shouldHave(text("Неверные учетные данные пользователя"));
        return this;
    }

    public void checkLoginPageTitle() {
        header.shouldBe(visible).shouldHave(text("Log in"));
    }
}
