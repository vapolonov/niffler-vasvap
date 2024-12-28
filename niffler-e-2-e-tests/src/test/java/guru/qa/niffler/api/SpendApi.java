package guru.qa.niffler.api;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface SpendApi {

    @POST("internal/spends/add")
    Call<SpendJson> addSpend(@Body SpendJson spend);

    @PATCH("internal/spends/edit")
    Call<SpendJson> editSpend(@Body SpendJson spend);

    @GET("internal/spends/{id}")
    Call<SpendJson> getSpendById(@Path("id") int id);

    @GET("internal/spends/all")
    Call<SpendJson> getSpendsAll(@Query("filterPeriod") DataFilterValues date, @Query("filterCurrency") CurrencyValues cur);

    @DELETE("internal/spends/remove")
    Call<Void> deleteSpends(@Query("ids") List<String> ids);

}
