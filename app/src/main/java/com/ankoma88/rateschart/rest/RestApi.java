package com.ankoma88.rateschart.rest;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;


/**
 * Created by ankoma88 on 18.06.16.
 */
public interface RestApi {
    @GET("connect.html?q=ozrates&c=EUR/USD&f=csv&s=n")
    Call<ResponseBody> loadData();
}

