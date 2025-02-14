package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import org.checkerframework.checker.units.qual.N;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage {

    private final String
            NO_USERS_FOUND = "There are no users yet",
            FRIENDS_TAB = "Friends";

    private final SelenideElement
            tableFriendsBody = $("#friends"),
            tableRequestsBody = $("#requests"),
            friendsTab = $$("[role='tablist'] h2").first(),
            allPeopleTab = $$("[role='tablist'] h2").get(1),
            noUsersText = $("[role='tabpanel'] p");

    public FriendsPage checkPageOpen() {
        friendsTab.shouldHave(text(FRIENDS_TAB));
        return this;
    }

    public void checkFriendName(String name) {
        tableFriendsBody.$$("tr").findBy(text(name)).shouldBe(visible);

    }

    public FriendsPage checkExistingFriends(String... expectedUsernames) {
        tableFriendsBody.$$("tr").shouldHave(textsInAnyOrder(expectedUsernames));
        return this;
    }

    public void checkNoFriends() {
        noUsersText.shouldHave(text(NO_USERS_FOUND));
    }

    public FriendsPage checkNoExistingFriends() {
        tableFriendsBody.$$("tr").shouldHave(size(0));
        return this;
    }

    public void checkIncomeRequests(String name) {
        tableRequestsBody.$$("tr").find(text(name)).shouldBe(visible);
    }

    public FriendsPage checkExistingInvitations(String... expectedUsernames) {
        tableRequestsBody.$$("tr").shouldHave(textsInAnyOrder(expectedUsernames));
        return this;
    }

    public AllPeoplePage selectAllPeopleTab() {
        allPeopleTab.click();
        return new AllPeoplePage();
    }

}
