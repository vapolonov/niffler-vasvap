package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.components.SearchField;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class AllPeoplePage {

    private final SelenideElement
            tableAllBody = $("#all"),
            peopleTab = $("a[href='/people/friends']"),
            allTab = $("a[href='/people/all']");

    private final SearchField searchInput = new SearchField();

    @Step("Проверить, что страница загрузилась")
    @Nonnull
    public AllPeoplePage checkThatPageLoaded() {
        peopleTab.shouldBe(Condition.visible);
        allTab.shouldBe(Condition.visible);
        return this;
    }

    @Step("Проверить запросы в друзья")
    public void checkOutputRequests(String name, String status) {
        tableAllBody.$$("tr").find(text(name)).shouldBe(visible);
        tableAllBody.$$("tr").find(text(status)).shouldBe(visible);
    }

    @Step("Проверить приглашение в друзья пользователю {0}")
    @Nonnull
    public AllPeoplePage checkInvitationSentToUser(String username) {
        SelenideElement friendRow = tableAllBody.$$("tr").find(text(username));
        friendRow.shouldHave(text("Waiting..."));
        return this;
    }

    @Step("отправить приглашение в друзья пользователю '{0}'")
    @Nonnull
    public AllPeoplePage sendFriendInvitationToUser(String username) {
        searchInput.search(username);
        SelenideElement friendRow = tableAllBody.$$("tr").find(text(username));
        friendRow.$(byText("Add friend")).click();
        return this;
    }

    @Step("Проверить, что пользователь '{0}' присутствует в таблице")
    @Nonnull
    public AllPeoplePage checkExistingUser(String username) {
        searchInput.search(username);
        tableAllBody.$$("tr").find(text(username)).should(visible);
        return this;
    }
}
