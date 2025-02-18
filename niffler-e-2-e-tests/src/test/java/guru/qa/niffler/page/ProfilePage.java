package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class ProfilePage {

    private final SelenideElement checkbox = $("input[type='checkbox']");
    private final ElementsCollection categoryNames = $$("[role='button']");

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
}
