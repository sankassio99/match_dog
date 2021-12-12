package com.example.matchdog.retrofit;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {

    private final Retrofit retrofit  ;

    public RetrofitConfig() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        OkHttpClient client = new OkHttpClient.Builder().build();
        this.retrofit = new Retrofit.Builder().baseUrl("https://api.thedogapi.com/v1/images/")
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .client(client)
                .build();

    }

    public DogService getCEPService() {
        return this.retrofit.create(DogService.class);
    }
}
