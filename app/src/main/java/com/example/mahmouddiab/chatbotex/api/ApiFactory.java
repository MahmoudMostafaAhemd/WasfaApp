package com.example.mahmouddiab.chatbotex.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mahmoud.diab on 11/29/2017.
 */

public class ApiFactory {


    private static ApiClient clientServices;

    private ApiFactory() {

    }

    public static ApiClient createInstance() {
        if (clientServices != null) {
            return clientServices;
        } else {
            clientServices = new Retrofit.Builder().baseUrl(ApiClient.baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(newGson()))
                    .build().create(ApiClient.class);
            return clientServices;

        }

    }

    private static Gson newGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }


    private static final class HeadersInterceptor implements Interceptor {
        private final String apiKey;

        HeadersInterceptor(String apiKey) {
            this.apiKey = apiKey;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {

            Request.Builder builder = chain.request().newBuilder();
            // add default header
            builder.addHeader("X_Api_Key", apiKey);
            builder.addHeader("Accept", "application/json");

            return chain.proceed(builder.build());

        }
    }
}
