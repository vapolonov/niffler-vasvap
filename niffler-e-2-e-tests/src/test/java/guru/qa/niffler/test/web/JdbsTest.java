package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
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
                                "test-cat-tx-1",
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

    @Test
    void xaTxTest() {
        UserDbClient usersDbClient = new UserDbClient();
        UserJson user = usersDbClient.createUser((
                new UserJson(
                        null,
                        "vasvap",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null
                )
        ));
        System.out.println(user);
    }
}

