package guru.qa.niffler.page.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class SearchField extends BaseComponent<SearchField> {

    protected SearchField(SelenideElement self) {
        super(self);
    }

    public SearchField() {
        super($("input[aria-label='search']"));
    }

    private final SelenideElement
            clearSearchBtn = $("#input-clear");


    @Nonnull
    @Step("Поиск записи в таблице по запросу '{0}'")
    public SearchField search(String query) {
        self.setValue(query).pressEnter();
        return this;
    }

    @Nonnull
    @Step("Очистить поел поиска")
    public SearchField clear() {
        if (self.is(not(empty))) {
            clearSearchBtn.click();
            self.shouldBe(empty);
        }
        return this;
    }

}
