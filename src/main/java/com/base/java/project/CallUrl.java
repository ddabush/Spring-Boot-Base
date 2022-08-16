package com.base.java.project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Callable;

public class CallUrl implements Callable {
    String q;
    int page;

    public CallUrl(String q, int page) {
        this.q = q;
        this.page = page;
    }


    @Override
    public String call(){
        String result = "";
        try {
                String qData = URLEncoder.encode(this.q, "UTF-8");
                URL url = new URL("https://api.github.com/search/code?q=" + qData+"&page="+this.page+"&per_page=100");
                String readLine = null;
                HttpURLConnection conection = (HttpURLConnection) url.openConnection();
                conection.setRequestMethod("GET");
                int responseCode = conection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conection.getInputStream()));
                    StringBuffer response = new StringBuffer();
                    while ((readLine = in.readLine()) != null) {
                        response.append(readLine);
                    }
                    in.close();
                    // print result
                    result = result+" "+response.toString();
                    Thread.sleep(10000);
                    //GetAndPost.POSTRequest(response.toString());
                } else {
                    System.out.println("GET NOT WORKED");
                }
        }catch(Exception e){
            System.out.println(e.getStackTrace());

        }

        return result;
    }
}
