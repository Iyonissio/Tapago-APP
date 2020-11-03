package com.tapago.app.rest;


import android.support.annotation.NonNull;


import com.tapago.app.interfaces.ApiService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author Keval on 9/2/16.
 */

public class RetrofitRestClient {

    private static final int TIME = 60;
    private static ApiService baseApiService = null;
    private static OkHttpClient httpClient;


    private static OkHttpClient getHttpClient() {
        httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(TIME, TimeUnit.SECONDS)
                .readTimeout(TIME, TimeUnit.SECONDS)
                .writeTimeout(TIME, TimeUnit.SECONDS)
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        Response response = chain.proceed(chain.request());
                        ResponseBody body = response.body();
                        String bodyString = body.string();
                        MediaType contentType = body.contentType();
                        //Log.d("Response", bodyString);
                        return response.newBuilder().body(ResponseBody.create(contentType, bodyString)).build();
                    }
                })
                .build();
        return httpClient;
    }

    public static ApiService getInstance() {
        if (baseApiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RestConstant.BASE_URL)
                    .addConverterFactory(new ToStringConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getHttpClient())
                    .build();

            baseApiService = retrofit.create(ApiService.class);
        }
        return baseApiService;
    }
}