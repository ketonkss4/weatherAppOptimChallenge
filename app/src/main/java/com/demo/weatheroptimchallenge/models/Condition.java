package com.demo.weatheroptimchallenge.models;

import com.demo.weatheroptimchallenge.networking.MainService;

import rx.Observable;

/**
 * Created by Kevin Moturi on 5/28/2016.
 */
public class Condition {

    private Observation current_observation;
    private Response response;

    public Response getResponse ()
    {
        return response;
    }



    public Observation getCurrent_observation ()
    {
        return current_observation;
    }

    public void setCurrent_observation (Observation current_observation)
    {
        this.current_observation = current_observation;
    }

    public static Observable<Condition> getNetworkRequest(String zip){
        return new MainService().requestCurrentCondition(zip);
    }

}
