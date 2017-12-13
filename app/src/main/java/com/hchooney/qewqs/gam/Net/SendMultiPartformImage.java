package com.hchooney.qewqs.gam.Net;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.google.android.gms.internal.zzagz.runOnUiThread;

/**
 * Created by qewqs on 2017-12-07.
 */

public class SendMultiPartformImage {
    private String resturl;
    private OkHttpClient client;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");


    private String URL="http://203.249.127.32:64001/mobile/";

    public SendMultiPartformImage(String resturl) {
        this.resturl = resturl;
        this.client = new OkHttpClient();
    }

    public String uploadFile(File image, String imageName, String user, int eid,
                             String date, String title) {
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", imageName, RequestBody.create(MEDIA_TYPE_PNG, image))
                .addFormDataPart("user", user)
                .addFormDataPart("eid", eid+"")
                .addFormDataPart("date", date)
                .addFormDataPart("title", title)
                .build();

        Request request = new Request.Builder().url(URL+resturl)
                .post(requestBody).build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!response.isSuccessful()) {
            try {
                throw new IOException("Unexpected code " + response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "None";
    }
}
