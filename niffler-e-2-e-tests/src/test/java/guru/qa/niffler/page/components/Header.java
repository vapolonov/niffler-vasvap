package guru.qa.niffler.page.components;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.*;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class Header extends BaseComponent<Header> {

    public Header() {
        super($("#root header"));
    }

    private final SelenideElement
            title = self.$("h1"),
            mainPageLink = self.$("a[href='/main']"),
            newSpendingBtn = self.$("a[href='/spending']"),
            profileBtn = $("a[href='/profile']"),
            friendsBtn = $("a[href='/people/friends']"),
            allPeopleBtn = $("a[href='/people/all']"),
            signOutBtn = $("a[href='/?']"),
            mainMenuBtn = self.$("button[aria-label='Menu']");


    @Step("Проверить заголовок")
    public void checkHeaderTitle() {
        title.shouldHave(text("Niffler"));
    }

    @Nonnull
    @Step("Открыть страницу 'Add new spending'")
    public EditSpendingPage toAddSpendingPage() {
        newSpendingBtn.click();
        return new EditSpendingPage();
    }

    @Nonnull
    @Step("Открыть страницу 'Profile'")
    public ProfilePage toProfilePage() {
        mainMenuBtn.click();
        profileBtn.click();
        return new ProfilePage();
    }

    @Nonnull
    @Step("Открыть страницу 'Friends'")
    public FriendsPage toFriendsPage() {
        mainMenuBtn.click();
        friendsBtn.click();
        return new FriendsPage();
    }

    @Nonnull
    @Step("Открыть страницу 'AllPeople'")
    public PeoplePage toAllPeoplePage() {
        mainMenuBtn.click();
        allPeopleBtn.click();
        return new PeoplePage();
    }

    @Nonnull
    @Step("Выход из личного кабинета")
    public LoginPage signOut() {
        mainMenuBtn.click();
        signOutBtn.click();
        return new LoginPage();
    }

    @Nonnull
    @Step("Открыть страницу главную страницу сайта")
    public MainPage toMainPage() {
        mainPageLink.click();
        return new MainPage();
    }
}
