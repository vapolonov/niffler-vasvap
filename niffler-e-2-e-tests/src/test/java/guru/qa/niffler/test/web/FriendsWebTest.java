package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.*;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;

@ExtendWith(BrowserExtension.class)
public class FriendsWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser user) {
        open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.password())
                .selectUserFriends()
                .checkPageOpen()
                .checkFriendName(user.friend());
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
        open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.password())
                .selectUserFriends()
                .checkPageOpen()
                .checkNoFriends();
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {
        open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.password())
                .selectUserFriends()
                .checkPageOpen()
                .checkIncomeRequests(user.income());
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void outcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
        open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.password())
                .selectUserFriends()
                .checkPageOpen()
                .selectAllPeopleTab()
                .checkOutputRequests(user.outcome(), "Waiting...");
    }
}