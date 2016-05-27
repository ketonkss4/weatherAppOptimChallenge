package com.demo.weatheroptimchallenge.networking;

import android.content.Context;

import com.demo.weatheroptimchallenge.models.Condition;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Kevin Moturi on 5/27/2016.
 */
public class MainService {
    public static final String API_BASE_URL = "http://api.wunderground.com/api/f7b2e04109149c28/";
    public static final String JSON = ".json";

    private NetworkService mNetworkService;

    public MainService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mNetworkService = retrofit.create(NetworkService.class);
    }

    private interface NetworkService {

        @GET("conditions")
        Observable<Condition> requestCurrentCondition(
                @Query("CA") String zip);

    }


    public Observable<Condition> requestCurrentCondition(String zip){
        return mNetworkService.requestCurrentCondition(zip+JSON);
    }
}
