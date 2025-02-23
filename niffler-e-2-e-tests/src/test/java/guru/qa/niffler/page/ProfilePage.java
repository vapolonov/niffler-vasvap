package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class ProfilePage extends BasePage<ProfilePage> {

    private final SelenideElement
            checkbox = $("input[type='checkbox']"),
            avatar = $("#image__input").parent().$("img"),
            userName = $("#username"),
            nameInput = $("#name"),
            photoInput = $("input[type='file']"),
            submitButton = $("button[type='submit']"),
            categoryInput = $("input[name='category']"),
            archivedSwitcher = $(".MuiSwitch-input");

    private final ElementsCollection
            bubbles = $$(".MuiChip-filled.MuiChip-colorPrimary"),
            bubblesArchived = $$(".MuiChip-filled.MuiChip-colorDefault"),
            categoryNames = $$("[role='button']");

    @Override
    @Step("Проверить, что страница загружена")
    @Nonnull
    public ProfilePage checkThatPageLoaded() {
        userName.should(visible);
        return this;
    }

    @Step("Выбрать чекбокс 'Show archived'")
    @Nonnull
    public ProfilePage selectCheckbox() {
        checkbox.click();
        return this;
    }

    @Step("Проверить категорию в списке")
    public void checkCategoryInList(String category) {
        categoryNames.findBy(text(category)).shouldBe(visible);
    }

    @Step("Загрузить картинку")
    @Nonnull
    public ProfilePage uploadPhotoFromClasspath(String path) {
        photoInput.uploadFromClasspath(path);
        return this;
    }

    @Step("Установить имя: '{0}'")
    @Nonnull
    public ProfilePage setName(String name) {
        nameInput.clear();
        nameInput.setValue(name);
        return this;
    }

    @Step("Сохранить данные")
    @Nonnull
    public ProfilePage submitProfile() {
        submitButton.click();
        return this;
    }

    @Step("Проверить установленное имя: '{0}'")
    @Nonnull
    public ProfilePage checkName(String name) {
        nameInput.shouldHave(value(name));
        return this;
    }

    @Step("Проверить отображение аватара")
    @Nonnull
    public ProfilePage checkPhotoExist() {
        avatar.should(attributeMatching("src", "data:image.*"));
        return this;
    }

    @Step("Добавить категорию: '{0}'")
    @Nonnull
    public ProfilePage addCategory(String category) {
        categoryInput.setValue(category).pressEnter();
        return this;
    }

    @Step("Проверить новую категорию: '{0}'")
    @Nonnull
    public ProfilePage checkCategoryExists(String category) {
        bubbles.find(text(category)).shouldBe(visible);
        return this;
    }

    @Step("Проверить, что больше нельзя добавлять категории")
    @Nonnull
    public ProfilePage checkThatCategoryInputDisabled() {
        categoryInput.should(disabled);
        return this;
    }
}
