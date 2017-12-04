package com.hchooney.qewqs.gam.Net;


import android.util.Log;

import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by hooney on 2017. 11. 29..
 */

public class SendPost {
    private String resturl;
    private JSONObject jsonObject;

    private String URL="http://203.249.127.32:64001/mobile/";

    private HttpURLConnection client;

    public SendPost(String resturl, JSONObject q) {
        this.resturl = resturl;
        this.jsonObject = q;
    }


    public String SendPost(){
        BufferedReader reader = null;
        try {
            URL url = new URL(URL+resturl);
            Log.d("POST URL", "POST URL : " + URL+resturl);
            client =(HttpURLConnection) url.openConnection();

            client.setRequestMethod("POST");
            client.setRequestProperty("Cache-Control", "no-cache");
            client.setRequestProperty("Content-Type", "application/json");

            client.setDoOutput(true);
            client.setDoInput(true);
            //client.connect();

            Log.d("POST JSON", this.jsonObject.toString());
            OutputStream os = client.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(jsonObject));

            client.connect();

            writer.flush();
            writer.close();
            os.close();

            int responseCode=client.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                InputStream stream = client.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                Log.d("Post", "Post : "+buffer.toString());

                Log.d("Send URL", URL+resturl);
                Log.d("Send URL Post", "Post : " +buffer.toString());

                return buffer.toString();
            }

            //서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(client != null) // Make sure the connection is not null.
                client.disconnect();
        }
        return "False";
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
