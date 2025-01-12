package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class JdbsTest {

    @Test
    public void daotest() {
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "test-cat-tx",
                                "vasvap",
                                false
                        ),
                        CurrencyValues.RUB,
                        3400.0,
                        "test tx",
                        "vasvap"
                )
        );
        System.out.println(spend);
    }
}
