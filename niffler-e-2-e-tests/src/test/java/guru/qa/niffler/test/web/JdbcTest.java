package guru.qa.niffler.test.web;

import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.SpendDbClient;
import guru.qa.niffler.service.impl.UsersDbClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;
import java.util.UUID;


public class JdbcTest {

    @Test
    void txTest() {
        SpendDbClient spendDbClient = new SpendDbClient();
        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "cat-name-tx-4",
                                "vasvap",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "spend-name-tx",
                        "vasvap"
                )
        );

        System.out.println(spend);
    }

    @Test
    void createCategoryTest() {
        SpendDbClient spendDbClient = new SpendDbClient();
        CategoryJson category = spendDbClient.createCategory(
                new CategoryJson(
                        null,
                        "test-category-123",
                        "oliver.leffler",
                        true
                )
        );
        System.out.println(category);
    }

    @Test
    void deleteCategoryTest() {
        SpendDbClient spendDbClient = new SpendDbClient();
        spendDbClient.removeCategory(
                new CategoryJson(
                        UUID.fromString("d996d488-e64b-11ef-ba8a-0242ac110004"),
                        "test-category-999",
                        "jim.zieme",
                        false
                )
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "valentin-7"
    })
    void addFriendshipRequestTest(String username) {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUser(
                username,
                "12345"
        );

        usersDbClient.createIncomeInvitation(user, 1);
        System.out.println(user);
    }

    @Test
    void addFriendTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUser(
                "valera-2",
                "12345"
        );

        usersDbClient.addFriend(user, 1);
        System.out.println(user);
    }
}
