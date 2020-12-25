package com.mydomain.yyjp.myfortuneapp.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
  private Retrofit retrofit = null;
  private NetworkApi api = null;

  private Retrofit getRetrofit() {
    if (retrofit == null) {
      retrofit = new Builder()
          .baseUrl("http://api.acme.international/")
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .addConverterFactory(GsonConverterFactory.create())
          .build();
    }
    return retrofit;
  }

    private OkHttpClient getHttpClient()
    {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        return okHttpClient;
    }

  public NetworkApi getApi() {
    if (api == null) {
      api = getRetrofit().create(NetworkApi.class);
    }
    return api;
  }
}