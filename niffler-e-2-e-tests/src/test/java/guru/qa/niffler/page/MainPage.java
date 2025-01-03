package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.WebDriverRunner.url;

public class MainPage {

    private final ElementsCollection
            tableRows = $("#spendings tbody").$$("tr"),
            pageTitles = $$("h2");

    private final SelenideElement
            menuButton = $("[aria-label='Menu']"),
            profileButton = $$(".nav-link").findBy(text("Profile")),
            friendsButton = $$(".nav-link").findBy(text("Friends"));

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(textCaseSensitive(spendingDescription)).$("td", 5).click();
        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.findBy(text(spendingDescription)).shouldBe(visible);
    }

    public MainPage checkThatMainPageHasStatistics() {
        pageTitles.findBy(textCaseSensitive("Statistics")).shouldBe(visible);
        return this;
    }

    public MainPage checkThatMainPageHasHistory() {
        pageTitles.findBy(textCaseSensitive("History of Spendings")).shouldBe(visible);
        return this;
    }

    public void checkMainPageUrl(String expUrl) {
        String currentUrl = url();
        assert currentUrl.equals(expUrl);
    }

    public ProfilePage selectUserProfile() {
        menuButton.click();
        profileButton.click();
        return new ProfilePage();
    }

    public FriendsPage selectUserFriends() {
        menuButton.click();
        friendsButton.click();
        return new FriendsPage();
    }
}
