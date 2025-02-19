package guru.qa.niffler.api.core;

import guru.qa.niffler.config.Config;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.lang.ArrayUtils;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import java.net.CookieManager;
import java.net.CookiePolicy;

public abstract class RestClient {

    protected final static Config CFG = Config.getInstance();

    private final OkHttpClient okHttpClient;
    protected final Retrofit retrofit;

    protected RestClient(String baseUrl) {
        this(baseUrl, false, JacksonConverterFactory.create(), HttpLoggingInterceptor.Level.BODY);
    }

    protected RestClient(String baseUrl, boolean followRedirect) {
        this(baseUrl, followRedirect, JacksonConverterFactory.create(), HttpLoggingInterceptor.Level.BODY);
    }

    protected RestClient(String baseUrl, Converter.Factory factory) {
        this(baseUrl, false, factory, HttpLoggingInterceptor.Level.BODY);
    }

    protected RestClient(
            String baseUrl,
            boolean followRedirect,
            Converter.Factory factory,
            HttpLoggingInterceptor.Level level,
            Interceptor... interceptors
    ) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .followRedirects(followRedirect);

        if (ArrayUtils.isNotEmpty(interceptors)) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }

        builder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(level));

        builder.cookieJar(
                new JavaNetCookieJar(
                        new CookieManager(
                                ThreadSafeCookieStore.INSTANCE,
                                CookiePolicy.ACCEPT_ALL
                        )
                )
        );

        this.okHttpClient = builder.build();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(factory)
                .build();
    }

    @Nonnull
    public <T> T create(final Class<T> service) {
        return this.retrofit.create(service);
    }

    public static final class EmtyRestClient extends RestClient {
        public EmtyRestClient(String baseUrl) {
            super(baseUrl);
        }

        public EmtyRestClient(String baseUrl, boolean followRedirect) {
            super(baseUrl, followRedirect);
        }

        public EmtyRestClient(String baseUrl, Converter.Factory factory) {
            super(baseUrl, factory);
        }

        public EmtyRestClient(String baseUrl, boolean followRedirect, Converter.Factory factory, HttpLoggingInterceptor.Level level, Interceptor... interceptors) {
            super(baseUrl, followRedirect, factory, level, interceptors);
        }
    }
}
