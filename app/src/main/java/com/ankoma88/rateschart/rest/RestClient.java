package com.ankoma88.rateschart.rest;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

/**
 * Created by ankoma88 on 18.06.16.
 */
public class RestClient {
    private static final String REST_BASE_URL = "http://webrates.truefx.com/rates/";
    private RestApi restApi;
    private Callback<ResponseBody> restCallback;

    public RestClient(Callback<ResponseBody> restCallback) {
        this.restCallback = restCallback;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(REST_BASE_URL)
                .build();

        restApi = retrofit.create(RestApi.class);

    }

    public void loadData() {
        Call<ResponseBody> call = restApi.loadData();
        call.enqueue(restCallback);
    }
}
