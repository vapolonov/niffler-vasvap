package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class FriendsPage extends BasePage<FriendsPage> {

    private final String
            NO_USERS_FOUND = "There are no users yet",
            FRIENDS_TAB = "Friends";

    private final SelenideElement
            tableFriendsBody = $("#friends"),
            tableRequestsBody = $("#requests"),
            friendsTab = $$("[role='tablist'] h2").first(),
            allPeopleTab = $$("[role='tablist'] h2").get(1),
            noUsersText = $("[role='tabpanel'] p"),
            popup = $("div[role='dialog']");

    @Override
    @Nonnull
    public FriendsPage checkThatPageLoaded() {
        friendsTab.shouldBe(visible);
        allPeopleTab.shouldBe(visible);
        return this;
    }

    @Step("проверить, что страница открыта")
    @Nonnull
    public FriendsPage checkPageOpen() {
        friendsTab.shouldHave(text(FRIENDS_TAB));
        return this;
    }

    @Step("проверить имя друга")
    public void checkFriendName(String name) {
        tableFriendsBody.$$("tr").findBy(text(name)).shouldBe(visible);

    }

    @Step("Проверить, что количество друзей равно {expectedCount}")
    @Nonnull
    public FriendsPage checkExistingFriendsCount(int expectedCount) {
        tableFriendsBody.$$("tr").shouldHave(size(expectedCount));
        return this;
    }

    @Step("Проверить существующих друзей")
    @Nonnull
    public FriendsPage checkExistingFriends(String... expectedUsernames) {
        tableFriendsBody.$$("tr").shouldHave(textsInAnyOrder(expectedUsernames));
        return this;
    }

    @Step("Проверить, что друзей нет")
    public void checkNoFriends() {
        noUsersText.shouldHave(text(NO_USERS_FOUND));
    }

    @Step("Проверить, что количество друзей равно 0")
    @Nonnull
    public FriendsPage checkNoExistingFriends() {
        tableFriendsBody.$$("tr").shouldHave(size(0));
        return this;
    }

    @Step("Проверить наличие запроса в друзья от {0}")
    public void checkIncomeRequests(String name) {
        tableRequestsBody.$$("tr").find(text(name)).shouldBe(visible);
    }

    @Step("Проверить текущие приглашения в друзья")
    @Nonnull
    public FriendsPage checkExistingInvitations(String... expectedUsernames) {
        tableRequestsBody.$$("tr").shouldHave(textsInAnyOrder(expectedUsernames));
        return this;
    }

    @Step("Выбрать вкладку 'All people'")
    @Nonnull
    public AllPeoplePage selectAllPeopleTab() {
        allPeopleTab.click();
        return new AllPeoplePage();
    }

    @Step("Удалить пользователя из друзей: {username}")
    @Nonnull
    public FriendsPage removeFriend(String username) {
        SelenideElement friendRow = tableFriendsBody.$$("tr").find(text(username));
        friendRow.$("button[type='button']").click();
        popup.$(byText("Delete")).click(usingJavaScript());
        return this;
    }

    @Step("Проверить, что количество приглашений равно {expectedCount}")
    @Nonnull
    public FriendsPage checkExistingInvitationsCount(int expectedCount) {
        tableRequestsBody.$$("tr").shouldHave(size(expectedCount));
        return this;
    }

    @Step("Accept invitation from user: {username}")
    @Nonnull
    public FriendsPage acceptFriendInvitationFromUser(String username) {
        SelenideElement friendRow = tableRequestsBody.$$("tr").find(text(username));
        friendRow.$(byText("Accept")).click();
        return this;
    }

    @Step("Decline invitation from user: {username}")
    @Nonnull
    public FriendsPage declineFriendInvitationFromUser(String username) {
        SelenideElement friendRow = tableRequestsBody.$$("tr").find(text(username));
        friendRow.$(byText("Decline")).click();
        popup.$(byText("Decline")).click(usingJavaScript());
        return this;
    }
}
