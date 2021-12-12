package com.example.matchdog.retrofit;

import com.example.matchdog.models.Dog;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface DogService {

    @Headers({"X-Api-Key: 1714b92d-d7a3-426f-a841-34ac5fe5fb3d"})
    @GET("search")
    Call<List<Dog>> getApiAleatoryDog();

    @Headers({"X-Api-Key: 1714b92d-d7a3-426f-a841-34ac5fe5fb3d"})
    @GET("{idDog}")
    Call<Dog> getApiDogById(@Path("idDog") String idDog);

}
