package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.PeoplePage;
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

    @User(friends = 1)
    @Test
    void shouldRemoveFriend(UserJson user) {
        final String userToRemove = user.testData().friendsUsernames()[0];

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .toFriendsPage()
                .removeFriend(userToRemove)
                .checkExistingFriendsCount(0);
    }

    @User(incomeInvitations = 1)
    @Test
    void shouldAcceptInvitation(UserJson user) {
        final String userToAccept = user.testData().incomeInvitationsUsernames()[0];

        FriendsPage friendsPage = open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .toFriendsPage()
                .checkExistingInvitationsCount(1)
                .acceptFriendInvitationFromUser(userToAccept)
                .checkExistingInvitationsCount(0);

        Selenide.refresh();

        friendsPage.checkExistingFriendsCount(1)
                .checkExistingFriends(userToAccept);
    }

    @User(incomeInvitations = 1)
    @Test
    void shouldDeclineInvitation(UserJson user) {
        final String userToDecline = user.testData().incomeInvitationsUsernames()[0];

        FriendsPage friendsPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .toFriendsPage()
                .checkExistingInvitationsCount(1)
                .declineFriendInvitationFromUser(userToDecline)
                .checkExistingInvitationsCount(0);

        Selenide.refresh();

        friendsPage.checkExistingFriendsCount(0);

        open(PeoplePage.URL, PeoplePage.class)
                .checkExistingUser(userToDecline);
    }
}
