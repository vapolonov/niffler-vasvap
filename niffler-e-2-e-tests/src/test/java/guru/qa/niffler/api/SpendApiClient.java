package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient {
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().spendUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    public SpendJson addSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.addSpend(spend).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(201, response.code());
        return response.body();
    }

    public SpendJson editSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.editSpend(spend).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public SpendJson getSpendById(int id) {
        final Response<SpendJson> response;
        try {
            response = spendApi.getSpendById(id).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public SpendJson getSpendsAll(DataFilterValues date, CurrencyValues cur) {
        final Response<SpendJson> response;
        try {
            response = spendApi.getSpendsAll(date, cur).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public void deleteSpends(List<String> ids) {
        final Response<Void> response;
        try {
            response = spendApi.deleteSpends(ids).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
    }
}
