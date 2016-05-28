package com.demo.weatheroptimchallenge.models;

/**
 * Created by Kevin Moturi on 5/28/2016.
 */
public class Response {
    private Error error;

    private String termsofService;

    private String version;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public class Error {
        private String description;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }

}