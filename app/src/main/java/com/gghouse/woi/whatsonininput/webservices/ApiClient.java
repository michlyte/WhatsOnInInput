package com.gghouse.woi.whatsonininput.webservices;

import com.gghouse.woi.whatsonininput.common.Config;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by michaelhalim on 9/17/16.
 */
public class ApiClient {
    private static Retrofit mRetrofit = null;

    public static ApiService getClient() {
        if (mRetrofit == null) {
            // Michael Halim : Dynamic header
//            Interceptor interceptor = new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request originalRequest = chain.request();
//
//                    String token = "Michael Halim";
//
//                    Request newRequest = originalRequest.newBuilder()
//                            .header("Authorization", token)
//                            .build();
//
//                    return chain.proceed(newRequest);
//                }
//            };

            // Michael Halim : Log
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(Config.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(Config.READ_TIMEOUT, TimeUnit.SECONDS)
//                    .addInterceptor(interceptor)
                    .addInterceptor(httpLoggingInterceptor)
                    .build();

            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Config.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit.create(ApiService.class);
    }
}