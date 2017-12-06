package com.hchooney.qewqs.gam.Net;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by hooney on 2017. 12. 6..
 */

public class SendPostReq {
    private String resturl;
    private JSONObject jsonObject;
    private OkHttpClient client;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private String URL="http://203.249.127.32:64001/mobile/";

    public SendPostReq(String resturl, JSONObject jsonObject) {
        this.resturl = resturl;
        this.jsonObject = jsonObject;
        this.client= new OkHttpClient();

        Log.d("POST URL", URL+resturl);
        Log.d("POST Json", this.jsonObject.toString());
    }


    public String post() throws IOException {
        RequestBody body = RequestBody.create(JSON, this.jsonObject.toString());
        Request request = new Request.Builder()
                .url(URL+resturl)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
