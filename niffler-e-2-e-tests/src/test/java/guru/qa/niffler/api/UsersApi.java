package guru.qa.niffler.api;

import guru.qa.niffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface UsersApi {

    @GET("internal/users/current")
    Call<UserJson> currentUser(@Query("username") String username);

    @POST("internal/users/update")
    Call<UserJson> updateUserInfo(@Body UserJson user);

    @GET("internal/users/all")
    Call<List<UserJson>> allUsers(@Query("sort") String username);

    @GET("internal/friends/all")
    Call<List<UserJson>> allFriends(@Query("sort") String username);

    @DELETE("internal/friends/remove")
    Call<Void> removeFriend(@Query("username") String username,
                            @Query("targetUsername") String targetUsername);

    @POST("internal/invitations/accept")
    Call<UserJson> acceptInvitation(@Query("username") String username,
                                    @Query("targetUsername") String targetUsername);

    @POST("internal/invitations/decline")
    Call<UserJson> declineInvitation(@Query("username") String username,
                                     @Query("targetUsername") String targetUsername);

    @POST("internal/invitations/send")
    Call<UserJson> sendInvitation(@Query("username") String username,
                                  @Query("targetUsername") String targetUsername);
}
