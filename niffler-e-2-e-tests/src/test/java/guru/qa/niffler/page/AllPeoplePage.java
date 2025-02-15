package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class AllPeoplePage {

    private final SelenideElement tableAllBody = $("#all");

    public void checkOutputRequests(String name, String status) {
        tableAllBody.$$("tr").find(text(name)).shouldBe(visible);
        tableAllBody.$$("tr").find(text(status)).shouldBe(visible);
    }
}
