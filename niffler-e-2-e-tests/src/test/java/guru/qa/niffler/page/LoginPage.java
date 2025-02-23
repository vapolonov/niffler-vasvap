package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class LoginPage extends BasePage<LoginPage> {

    private final SelenideElement
            usernameInput = $("input[name='username']"),
            passwordInput = $("input[name='password']"),
            submitButton = $("button[type='submit']"),
            createAccountButton = $(".form__register"),
            errorMessage = $(".form__error"),
            header = $(".header");

    @Override
    @Step("Проверить, что страница загружена")
    @Nonnull
    public LoginPage checkThatPageLoaded() {
        usernameInput.should(visible);
        passwordInput.should(visible);
        return this;
    }

    @Step("Авторизация пользователя {0}, {1}")
    @Nonnull
    public MainPage doLogin(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
        return new MainPage();
    }

    @Step("Авторизация пользователя c некорректными данными")
    @Nonnull
    public LoginPage doLoginWithBadCredentials(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
        return this;
    }

    @Step("Создать новый аккаунт")
    @Nonnull
    public RegisterPage createAccount() {
        createAccountButton.click();
        return new RegisterPage();
    }

    @Step("Проверить ошибку при неверном вводе данных")
    @Nonnull
    public LoginPage checkBadCredentialsErrorMsg() {
        errorMessage.shouldBe(visible).shouldHave(text("Неверные учетные данные пользователя"));
        return this;
    }

    @Step("Проверить заголовок страницы логина")
    public void checkLoginPageTitle() {
        header.shouldBe(visible).shouldHave(text("Log in"));
    }
}
