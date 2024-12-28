package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {

    private final SelenideElement checkbox = $("input[type='checkbox']");
    private final ElementsCollection categoryNames = $$("[role='button']");

    public ProfilePage selectCheckbox() {
        checkbox.click();
        return this;
    }

    public void checkCategoryInList(String category) {
        categoryNames.findBy(text(category)).shouldBe(visible);
    }
}
