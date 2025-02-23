package guru.qa.niffler.page.components;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.page.EditSpendingPage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class SpendingTable extends BaseComponent<SpendingTable> {

    public SpendingTable() {
        super($("#spendings"));
    }

    SearchField searchField = new SearchField();

    private final SelenideElement
            dropdownList = $("[role='listbox']"),
            selectPeriod = $("[name='period']"),
            deleteBtn = $("#delete"),
            popup = $("div[role='dialog']");

    private final ElementsCollection tableRows = self.$("tbody").$$("tr");

    @Nonnull
    @Step("Выбрать период для поиска: '{0}'")
    public SpendingTable selectPeriod(DataFilterValues period) {
        selectPeriod.click();
        dropdownList.$(byText(period.text)).click();
        return this;
    }

    @Nonnull
    @Step("Редактировать расход с описанием '{0}'")
    public EditSpendingPage editSpending(String description) {
        searchSpendingByDescription(description);
        tableRows.findBy(text(description)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    @Nonnull
    @Step("Удалить расход с описанием '{0}'")
    public SpendingTable deleteSpending(String description) {
        searchSpendingByDescription(description);
        tableRows.findBy(text(description)).$("[type='checkbox']").click();
        deleteBtn.click();
        popup.$(byText("Delete")).click(usingJavaScript());
        return this;
    }

    @Nonnull
    @Step("Найти расход с описанием '{0}'")
    public SpendingTable searchSpendingByDescription(String description) {
        searchField.search(description);
        return this;
    }

    @Nonnull
    @Step("Проверить, что в таблице есть данные '{0}'")
    public SpendingTable checkTableContains(String expectedSpend) {
        self.$$("tr").findBy(text(expectedSpend)).shouldBe(visible);
        return this;
    }

    @Nonnull
    @Step("Проверить, что что в таблице {0} строк")
    public SpendingTable checkTableSize(int expectedSize) {
        tableRows.shouldHave(size(expectedSize));
        return this;
    }
}
