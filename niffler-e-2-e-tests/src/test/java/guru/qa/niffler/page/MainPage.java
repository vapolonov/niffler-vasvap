package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.components.Header;
import guru.qa.niffler.page.components.SearchField;
import guru.qa.niffler.page.components.SpendingTable;
import guru.qa.niffler.page.components.StatComponent;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.WebDriverRunner.url;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {

    private final ElementsCollection
            tableRows = $("#spendings tbody").$$("tr"),
            pageTitles = $$("h2");

    private final SelenideElement
            menuButton = $("[aria-label='Menu']"),
            profileButton = $$(".nav-link").findBy(text("Profile")),
            friendsButton = $$(".nav-link").findBy(text("Friends")),
            allPeopleBtn = $$(".nav-link").findBy(text("All People"));

    protected final SearchField searchField = new SearchField();
    protected final Header header = new Header();
    protected final SpendingTable spendingTable = new SpendingTable();
    protected final StatComponent statComponent = new StatComponent();

    @Nonnull
    public Header getHeader() {
        return header;
    }

    @Nonnull
    public SpendingTable getSpendingTable() {
        spendingTable.getSelf().scrollIntoView(true);
        return spendingTable;
    }

    @Override
    @Step("Проверить, что страница загружена")
    @Nonnull
    public MainPage checkThatPageLoaded() {
        header.getSelf().should(visible).shouldHave(text("Niffler"));
        statComponent.getSelf().should(visible).shouldHave(text("Statistics"));
        spendingTable.getSelf().should(visible).shouldHave(text("History of Spendings"));
        return this;
    }

    @Step("Редактировать расход")
    @Nonnull
    public EditSpendingPage editSpending(String spendingDescription) {
        searchField.search(spendingDescription);
        tableRows.find(textCaseSensitive(spendingDescription)).$("td", 5).click();
        return new EditSpendingPage();
    }

    @Step("Редактировать расход")
    @Nonnull
    public MainPage checkThatTableContainsSpending(String spendingDescription) {
        tableRows.findBy(text(spendingDescription)).shouldBe(visible);
        return this;
    }

    @Step("Проверить, что на главной странице есть статистика")
    @Nonnull
    public MainPage checkThatMainPageHasStatistics() {
        pageTitles.findBy(textCaseSensitive("Statistics")).shouldBe(visible);
        return this;
    }

    @Step("Проверить, что на главное странице есть история расходов")
    @Nonnull
    public MainPage checkThatMainPageHasHistory() {
        pageTitles.findBy(textCaseSensitive("History of Spendings")).shouldBe(visible);
        return this;
    }

    @Step("Проверить URL главной страницы")
    @Nonnull
    public MainPage checkMainPageUrl(String expUrl) {
        String currentUrl = url();
        assert currentUrl.equals(expUrl);
        return this;
    }

    @Step("Перейти в профиль пользователя")
    @Nonnull
    public ProfilePage selectUserProfile() {
        menuButton.click();
        profileButton.click();
        return new ProfilePage();
    }

    @Step("Перейти на страницу 'Friends'")
    @Nonnull
    public FriendsPage selectUserFriends() {
        menuButton.click();
        friendsButton.click();
        return new FriendsPage();
    }

    @Step("Перейти на страницу 'All people'")
    @Nonnull
    public AllPeoplePage selectAllPeople() {
        menuButton.click();
        allPeopleBtn.click();
        return new AllPeoplePage();
    }
}
