package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import org.checkerframework.checker.units.qual.N;

import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage {

    private final String
            NO_USERS_FOUND = "There are no users yet",
            FRIENDS_TAB = "Friends";

    private final SelenideElement
            tableFriendsBody = $("#friends"),
            tableAllBody = $("#all"),
            tableRequestsBody = $("#requests"),
            friendsTab = $$("[role='tablist'] h2").first(),
            allPeopleTab = $$("[role='tablist'] h2").get(1),
            noUsersText = $("[role='tabpanel'] p");

    public FriendsPage checkPageOpen() {
        friendsTab.shouldHave(text(FRIENDS_TAB));
        return this;
    }

    public void checkFriendName(String name) {
        tableFriendsBody.$("tr").shouldHave(text(name));
    }

//    public FriendsPage checkExistingFriends(String... expectedUsernames) {
//        tableFriendsBody.$$("tr").shouldHave(textsInAnyOrder(expectedUsernames));
//        return this;
//    }

    public void checkNoFriends() {
        noUsersText.shouldHave(text(NO_USERS_FOUND));
    }

    public void checkIncomeRequests(String name) {
        tableRequestsBody.$$("tr").first().shouldHave(text(name));
    }

    public FriendsPage selectAllPeopleTab() {
        allPeopleTab.click();
        return this;
    }

    public void checkOutputRequests(String name, String status) {
        tableAllBody.$$("tr").first().$$("td").first().shouldHave(text(name));
        tableAllBody.$$("tr").first().$$("td").get(1).shouldHave(text(status));

    }
}
