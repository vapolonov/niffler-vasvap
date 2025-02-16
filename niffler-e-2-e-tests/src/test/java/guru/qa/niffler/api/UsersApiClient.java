package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsersApiClient {

    private final Retrofit retrofitUser = new Retrofit.Builder()
            .baseUrl(Config.getInstance().userdataUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final UsersApi usersApi = retrofitUser.create(UsersApi.class);

    @Step("Получить пользователя по имени через API")
    @NotNull
    public UserJson getCurrentUser(String username) {
        final Response<UserJson> response;
        try {
            response = usersApi.currentUser(username).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return requireNonNull(response.body());
    }

    @Step("Редактировать пользователя через API")
    @NotNull
    public UserJson updateUser(UserJson user) {
        final Response<UserJson> response;
        try {
            response = usersApi.updateUserInfo(user).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return requireNonNull(response.body());
    }

    @Step("Получить список всех пользователей через API")
    @NotNull
    public List<UserJson> getAllUsers(String username) {
        final Response<List<UserJson>> response;
        try {
            response = usersApi.allUsers(username).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return requireNonNull(response.body());
    }

    @Step("Получить всех друзей пользователя через API")
    @NotNull
    public List<UserJson> getAllFriends(String username) {
        final Response<List<UserJson>> response;
        try {
            response = usersApi.allFriends(username).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return requireNonNull(response.body());
    }

    @Step("Удалить друга через API")
    public void removeFriend(String username, String targetUsername) {
        final Response<Void> response;
        try {
            response = usersApi.removeFriend(username, targetUsername).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
    }

    @Step("Принять приглашение дружбы через API")
    @NotNull
    public UserJson acceptInvitation(String username, String targetUsername) {
        final Response<UserJson> response;
        try {
            response = usersApi.acceptInvitation(username, targetUsername).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return requireNonNull(response.body());
    }

    @Step("Отклонить приглашение дружбы через API")
    @NotNull
    public UserJson declineInvitation(String username, String targetUsername) {
        final Response<UserJson> response;
        try {
            response = usersApi.declineInvitation(username, targetUsername).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return requireNonNull(response.body());
    }

    @Step("Отправить приглашение дружбы через API")
    @NotNull
    public UserJson sendInvitation(String username, String targetUsername) {
        final Response<UserJson> response;
        try {
            response = usersApi.sendInvitation(username, targetUsername).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return requireNonNull(response.body());
    }
}
