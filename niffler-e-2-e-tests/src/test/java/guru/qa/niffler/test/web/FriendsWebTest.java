package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

@WebTest
public class FriendsWebTest {

    private static final Config CFG = Config.getInstance();

    @User(friends = 1)
    @Test
    void friendShouldBePresentInFriendsTable(UserJson user) {
        final String friendUsername = user.testData().friendsUsernames()[0];

        open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .selectUserFriends()
                .checkPageOpen()
                .checkFriendName(friendUsername);
    }

    @User
    @Test
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .selectUserFriends()
                .checkPageOpen()
                .checkNoFriends();
    }

    @User(incomeInvitations = 1)
    @Test
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        final String friendUsername = user.testData().incomeInvitationsUsernames()[0];
        open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .selectUserFriends()
                .checkPageOpen()
                .checkIncomeRequests(friendUsername);
    }

    @User(outcomeInvitations = 1)
    @Test
    void outcomeInvitationBePresentInAllPeoplesTableStaticUser(UserJson user) {
        final String friendUsername = user.testData().outcomeInvitationsUsernames()[0];
        open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .selectUserFriends()
                .checkPageOpen()
                .selectAllPeopleTab()
                .checkOutputRequests(friendUsername, "Waiting...");
    }
}
