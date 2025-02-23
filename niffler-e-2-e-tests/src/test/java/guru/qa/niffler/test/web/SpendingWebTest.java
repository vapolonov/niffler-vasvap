package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.test.web.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static com.codeborne.selenide.Selenide.open;

@WebTest
public class SpendingWebTest {

    private static final Config CFG = Config.getInstance();

    @User(
            spendings = {@Spending(
                    category = "Courses",
                    description = "java advanced 2",
                    amount = 50000
            )}
    )
    @Test
    public void categoryDescriptionShouldBeChangedFromTable(UserJson user) {
        final String newDescription = "Обучение Java Advanced 2.0";
        open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .editSpending("java advanced 2")
                .setNewSpendingDescription(newDescription)
                .save()
                .checkThatTableContainsSpending(newDescription);

        new MainPage().getSpendingTable()
                .checkTableContains(newDescription);
    }

    @User
    @Test
    void shouldAddNewSpending(UserJson user) {
        String category = "Friends";
        int amount = 100;
        Date currentDate = new Date();
        String description = RandomDataUtils.randomSentence(3);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.testData().password())
                .getHeader()
                .toAddSpendingPage()
                .setNewSpendingCategory(category)
                .setNewSpendingAmount(amount)
                .setNewSpendingDate(currentDate)
                .setNewSpendingDescription(description)
                .save()
                .checkAlertMessage("New spending is successfully created");

        new MainPage().getSpendingTable()
                .checkTableContains(description);
    }
}
