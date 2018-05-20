package com.dankook.jalgashoe.util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by yeseul on 2018-02-17.
 */

public class HttpClient {
    private static final String BASE_URL = "http://52.55.118.238:8080/jShoe";
    private static AsyncHttpClient client = new AsyncHttpClient();

    private HttpClient(){

    }

    public static AsyncHttpClient getInstance(){

        return client;
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String url){
        return BASE_URL + url;
    }
}
