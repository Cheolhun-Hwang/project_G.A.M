package com.hchooney.qewqs.gam.Net;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by hooney on 2017. 11. 29..
 */

public class SendGet {
    private String resturl;
    private String queryCode;

    private String URL="http://203.249.127.32:64001/mobile/";

    public SendGet(String resturl, String queryCode) {
        this.resturl = resturl;
        this.queryCode = queryCode;
    }

    public String SendGet(){
        InputStream is;
        BufferedReader reader;
        StringBuilder sb;
        try {
            URL url = new URL(URL+resturl+queryCode); //문자열로 된 요청 url을 URL 객체로 생성.
            is = url.openStream();  //url위치로 입력스트림 연결

            reader = new BufferedReader(new InputStreamReader(is));
            sb = new StringBuilder();

            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "None";
        }

        Log.d("Send URL", URL+resturl+queryCode);
        Log.d("Send URL GET", "GET : " +sb.toString());
        return sb.toString();
    }
}
