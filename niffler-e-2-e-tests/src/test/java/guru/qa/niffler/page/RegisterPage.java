package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class RegisterPage extends BasePage<RegisterPage> {

    private final SelenideElement
            usernameInput = $("#username"),
            passwordInput = $("#password"),
            passwordSubmitInput = $("#passwordSubmit"),
            submitButton = $(".form__submit"),
            successText = $(".form__paragraph_success"),
            signinButton = $(".form_sign-in"),
            errorMessage = $(".form__error");

    @Step("Проверить, что страница загружена")
    @Override
    @Nonnull
    public RegisterPage checkThatPageLoaded() {
        usernameInput.should(visible);
        passwordInput.should(visible);
        passwordSubmitInput.should(visible);
        return this;
    }

    @Step("Ввести имя пользователя")
    @Nonnull
    public RegisterPage setUserName(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Step("Ввести пароль пользователя")
    @Nonnull
    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Повторить ввод пароля пользователя")
    @Nonnull
    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.setValue(password);
        return this;
    }

    @Step("Нажать на кнопку 'Sign Up'")
    @Nonnull
    public RegisterPage submitRegistration() {
        submitButton.click();
        return this;
    }

    @Step("проверить успешную регистрацию")
    @Nonnull
    public LoginPage successRegistration() {
        successText.shouldHave(text("Congratulations! You've registered!"));
        signinButton.click();
        return new LoginPage();
    }

    @Step("Проверить появление ошибки, что имя пользователя уже существует")
    public void checkUserExistsErrorMsg(String username) {
        errorMessage.shouldHave(text(String.format("Username `%s` already exists", username)));
    }

    @Step("Проверка ошибки при несовпадении паролей")
    public void checkPasswordAndConfirmPasswordAreNotEqualErrorMsg() {
        errorMessage.shouldHave(text("Passwords should be equal"));
    }
}
