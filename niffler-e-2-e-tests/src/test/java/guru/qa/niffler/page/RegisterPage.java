package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {

    private final SelenideElement
            usernameInput = $("#username"),
            passwordInput = $("#password"),
            passwordSubmitInput = $("#passwordSubmit"),
            submitButton = $(".form__submit"),
            successText = $(".form__paragraph_success"),
            signinButton = $(".form_sign-in"),
            errorMessage = $(".form__error");

    public RegisterPage setUserName(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.setValue(password);
        return this;
    }

    public RegisterPage submitRegistration() {
        submitButton.click();
        return this;
    }

    public LoginPage successRegistration() {
        successText.shouldHave(text("Congratulations! You've registered!"));
        signinButton.click();
        return new LoginPage();
    }

    public void checkUserExistsErrorMsg(String username) {
        errorMessage.shouldHave(text(String.format("Username `%s` already exists", username)));
    }

    public void checkPasswordAndConfirmPasswordAreNotEqualErrorMsg() {
        errorMessage.shouldHave(text("Passwords should be equal"));
    }
}
