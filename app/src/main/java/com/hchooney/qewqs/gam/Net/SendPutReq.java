package com.hchooney.qewqs.gam.Net;

import android.util.Log;


import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by qewqs on 2017-12-07.
 */

public class SendPutReq {
    private String resturl;
    private JSONObject jsonObject;
    private OkHttpClient client;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String URL="http://203.249.127.32:64001/mobile/";

    public SendPutReq(String resturl, JSONObject jsonObject) {
        this.resturl = resturl;
        this.jsonObject = jsonObject;
        this.client= new OkHttpClient();

        Log.d("POST URL", URL+resturl);
        Log.d("POST Json", this.jsonObject.toString());
    }

    public String put() throws IOException {
        RequestBody body = RequestBody.create(JSON, this.jsonObject.toString());
        Request request = new Request.Builder()
                .url(URL+resturl)
                .put(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
