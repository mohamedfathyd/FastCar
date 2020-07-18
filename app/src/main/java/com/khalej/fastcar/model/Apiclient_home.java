package com.khalej.fastcar.model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Apiclient_home {
    //http://jamalah.com/ http://jamalah.com/montag/Hoguzat
    // http://jamalah.com/montag/bazelt
    private static final String url="https://applicationme.com/";

    private static Retrofit retrofit =null;
    public static Retrofit getapiClient(){
        if(retrofit== null){
            retrofit=new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
