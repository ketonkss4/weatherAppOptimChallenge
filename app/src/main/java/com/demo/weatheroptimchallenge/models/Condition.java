package com.demo.weatheroptimchallenge.models;

import com.demo.weatheroptimchallenge.networking.MainService;
import com.google.gson.annotations.SerializedName;

import rx.Observable;

/**
 * Created by Kevin Moturi on 5/27/2016.
 */
public class Condition {

    @SerializedName("display_location")
    Location location;
    @SerializedName("observation_time")
    String observationTime;
    @SerializedName("weather")
    String condition;
    @SerializedName("temperature_string")
    String temp;
    @SerializedName("relative_humidity")
    String humidity;
    @SerializedName("icon_url")
    String iconUrl;

    public Location getLocation() {
        return location;
    }

    public String getObservationTime() {
        return observationTime;
    }

    public String getCondition() {
        return condition;
    }

    public String getTemp() {
        return temp;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public static Observable<Condition> getNetworkRequest(String zip){
        return new MainService().requestCurrentCondition(zip);
    }
}
