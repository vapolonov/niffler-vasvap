package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.components.Calendar;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.Date;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class EditSpendingPage extends BasePage<EditSpendingPage> {

    private final SelenideElement
            descriptionInput = $("#description"),
            saveBtn = $("#save"),
            amountInput = $("#amount"),
            categoryInput = $("#category"),
            cancelBtn = $("#cancel");

    private final ElementsCollection categories = $$(".MuiChip-root");
    private final Calendar calendar = new Calendar();

    @Override
    @Nonnull
    public EditSpendingPage checkThatPageLoaded() {
        amountInput.should(visible);
        return this;
    }

    @Step("Добавить новое описание расходу")
    public EditSpendingPage setNewSpendingDescription(String description) {
        descriptionInput.clear();
        descriptionInput.setValue(description);
        return this;
    }

    @Step("Select new spending category: '{0}'")
    @Nonnull
    public EditSpendingPage setNewSpendingCategory(String category) {
        categoryInput.clear();
        categoryInput.setValue(category);
        return this;
    }

    @Step("Set new spending amount: '{0}'")
    @Nonnull
    public EditSpendingPage setNewSpendingAmount(double amount) {
        amountInput.clear();
        amountInput.setValue(String.valueOf(amount));
        return this;
    }

    @Step("Set new spending date: '{0}'")
    @Nonnull
    public EditSpendingPage setNewSpendingDate(Date date) {
        calendar.selectDateInCalendar(date);
        return this;
    }

    @Step("Сохранить изменения")
    public MainPage save() {
        saveBtn.click();
        return new MainPage();
    }


}
