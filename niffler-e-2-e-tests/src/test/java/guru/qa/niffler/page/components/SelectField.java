package guru.qa.niffler.page.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class SelectField extends BaseComponent<SelectField> {

    public SelectField(SelenideElement self) {
        super(self);
    }

    private final SelenideElement input = self.$("input");

    @Step("Выбрать значение '{0}' из выпадающего списка")
    public void setValue(String value) {
        self.click();
        $$("li[role='option']").find(text(value)).click();
    }

    @Step("Проверить, что выбранное значение равно '{0}'")
    public void checkSelectValueIsEqualTo(String value) {
        self.shouldHave(text(value));
    }
}
