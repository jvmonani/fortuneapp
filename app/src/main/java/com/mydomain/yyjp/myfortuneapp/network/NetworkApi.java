package com.mydomain.yyjp.myfortuneapp.network;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NetworkApi {

  @GET("fortune")
  Call<FortuneResponse> getFortune();
}
