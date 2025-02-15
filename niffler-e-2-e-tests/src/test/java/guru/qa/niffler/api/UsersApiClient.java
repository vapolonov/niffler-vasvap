package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsersApiClient {

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().userdataUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final UsersApi usersApi = retrofit.create(UsersApi.class);

    public UserJson getCurrentUser(String username) {
        final Response<UserJson> response;
        try {
            response = usersApi.currentUser(username).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public UserJson updateUser(UserJson user) {
        final Response<UserJson> response;
        try {
            response = usersApi.updateUserInfo(user).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public List<UserJson> getAllUsers(String username) {
        final Response<List<UserJson>> response;
        try {
            response = usersApi.allUsers(username).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public List<UserJson> getAllFriends(String username) {
        final Response<List<UserJson>> response;
        try {
            response = usersApi.allFriends(username).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public void removeFriend(String username, String targetUsername) {
        final Response<Void> response;
        try {
            response = usersApi.removeFriend(username, targetUsername).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
    }

    public UserJson acceptInvitation(String username, String targetUsername) {
        final Response<UserJson> response;
        try {
            response = usersApi.acceptInvitation(username, targetUsername).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public UserJson declineInvitation(String username, String targetUsername) {
        final Response<UserJson> response;
        try {
            response = usersApi.declineInvitation(username, targetUsername).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public UserJson sendInvitation(String username, String targetUsername) {
        final Response<UserJson> response;
        try {
            response = usersApi.sendInvitation(username, targetUsername).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }


}
