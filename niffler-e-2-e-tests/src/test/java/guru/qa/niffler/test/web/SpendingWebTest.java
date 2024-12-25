package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.jupiter.Spending;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith(BrowserExtension.class)
public class SpendingWebTest {

    private static final Config CFG = Config.getInstance();

    @Spending(
            username = "vasvap",
            category = "Courses",
            description = "java advanced",
            amount = 50000
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
