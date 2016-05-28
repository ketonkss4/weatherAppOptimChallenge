package com.demo.weatheroptimchallenge.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.demo.weatheroptimchallenge.networking.MainService;
import com.google.gson.annotations.SerializedName;
import com.demo.weatheroptimchallenge.BR;

import rx.Observable;

/**
 * Created by Kevin Moturi on 5/27/2016.
 */
public class Observation extends BaseObservable{

    Location display_location;
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

    @Bindable
    public Location getLocation() {
        return display_location;
    }
    @Bindable
    public String getObservationTime() {
        return observationTime;
    }
    @Bindable
    public String getCondition() {
        return condition;
    }
    @Bindable
    public String getTemp() {
        return temp;
    }
    @Bindable
    public String getHumidity() {
        return "Humidity "+humidity;
    }
    @Bindable
    public String getIconUrl() {
        return iconUrl;
    }

    public void setLocation(Location location) {
        this.display_location = location;
        notifyPropertyChanged(BR.location);
    }

    public void setObservationTime(String observationTime) {
        this.observationTime = observationTime;
        notifyPropertyChanged(BR.observationTime);
    }

    public void setCondition(String condition) {
        this.condition = condition;
        notifyPropertyChanged(BR.condition);
    }

    public void setTemp(String temp) {
        this.temp = temp;
        notifyPropertyChanged(BR.temp);
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
        notifyPropertyChanged(BR.humidity);
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
        notifyPropertyChanged(BR.iconUrl);
    }
}
