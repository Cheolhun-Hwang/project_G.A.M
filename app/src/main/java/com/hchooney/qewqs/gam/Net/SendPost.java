package com.hchooney.qewqs.gam.Net;


import android.util.Log;

import java.io.BufferedOutputStream;
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

/**
 * Created by hooney on 2017. 11. 29..
 */

public class SendPost {
    private String resturl;
    private String queryCode;

    private String URL="http://203.249.127.32:64001/mobile/";

    private HttpURLConnection client;

    public SendPost(String resturl, String queryCode) {
        this.resturl = resturl;
        this.queryCode = queryCode;
    }


    public String SendPost(){
        BufferedReader reader = null;
        try {
            URL url = new URL(URL+resturl+queryCode);
            client =(HttpURLConnection) url.openConnection();

            client.setRequestMethod("POST");
            client.setRequestProperty("Accept", "text/html");

            client.setDoOutput(true);
            client.setDoInput(true);
            client.connect();

            //서버로 보내기위해서 스트림 만듬
            //OutputStream outStream = client.getOutputStream();
            //버퍼를 생성하고 넣음
            //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
            //writer.flush();
            //writer.close();//버퍼를 받아줌
            //서버로 부터 데이터를 받음

            InputStream stream = client.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while((line = reader.readLine()) != null){
                buffer.append(line);
            }

            Log.d("Post", "Post : "+buffer.toString());

            Log.d("Send URL", URL+resturl+queryCode);
            Log.d("Send URL Post", "Post : " +buffer.toString());

            return buffer.toString();
            //서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(client != null) // Make sure the connection is not null.
                client.disconnect();
        }
        return "False";
    }
}
