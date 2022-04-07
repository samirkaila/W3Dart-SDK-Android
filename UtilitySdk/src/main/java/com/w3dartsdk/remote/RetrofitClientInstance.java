package com.w3dartsdk.remote;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
    This is an a class from where API related code is initialized
 */
public class RetrofitClientInstance {

    private static Retrofit mRetrofit;

    private RetrofitClientInstance() {
    }

    public static String getRawURL(HttpUrl url) {
        return url.toString().replace("/", "\\");
    }

    public static Retrofit getRetrofitInstance() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                /*.addInterceptor(new AccessTokenInterceptor())*/
                .build();
        client.connectionPool().evictAll();
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(ApiConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return mRetrofit;
    }


}
