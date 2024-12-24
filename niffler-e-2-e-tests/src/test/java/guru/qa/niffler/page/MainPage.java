package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final ElementsCollection
            tableRows = $("#spendings tbody").$$("tr");

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(textCaseSensitive(spendingDescription)).$("td", 5).click();
        return new EditSpendingPage();
    }


    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.findBy(text(spendingDescription)).shouldBe(visible);
    }
}
