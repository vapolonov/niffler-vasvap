package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

@WebTest
public class SpendingWebTest {

    private static final Config CFG = Config.getInstance();

    @User(
            username = "vasvap",
            spendings = {@Spending(
                    category = "Courses",
                    description = "java advanced",
                    amount = 50000
            )}
    )
    @Test
    public void categoryDescriptionShouldBeChangedFromTable(SpendJson spend) {
        final String newDescription = "Java Advanced 2.0";
        open(CFG.frontUrl(), LoginPage.class)
                .doLogin("vasvap", "12345")
                .editSpending(spend.description())
                .setNewSpendingDescription(newDescription)
                .save()
                .checkThatTableContainsSpending(newDescription);
    }
}
