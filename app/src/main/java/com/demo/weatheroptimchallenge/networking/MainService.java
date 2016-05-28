package com.demo.weatheroptimchallenge.networking;

import com.demo.weatheroptimchallenge.models.Condition;
import com.demo.weatheroptimchallenge.models.Observation;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Kevin Moturi on 5/27/2016.
 */
public class MainService {
    public static final String API_BASE_URL = "http://api.wunderground.com/api/f7b2e04109149c28/";
    public static final String JSON = ".json";

    private NetworkService mNetworkService;

    public MainService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …

// add logging as last interceptor
        httpClient.addInterceptor(logging);// <-- this is the important line!

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        mNetworkService = retrofit.create(NetworkService.class);
    }

    private interface NetworkService {

        @GET("conditions/q/CA/{zip}")
        Observable<Condition> requestCurrentCondition(
                @Path("zip") String zip);

    }


    public Observable<Condition> requestCurrentCondition(String zip){
        return mNetworkService.requestCurrentCondition(zip+JSON);
    }
}
