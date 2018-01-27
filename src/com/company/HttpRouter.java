package com.company;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Router Http do łączenia się z API NBP.
 */

public class HttpRouter {


    public HttpRouter() {
    }

    private String StreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null)
            sb.append(line).append('\n');


        return sb.toString();
    }

    /**
     * Wysyła żądanie http i zwraca odpowiedź jako String.
     * @param givenUrl
     * @return String Json
     * @throws IOException
     */


    public String call(String givenUrl) throws IOException {
        String response = null;

        URL url = new URL(givenUrl);
        HttpURLConnection connection =(HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream in = new BufferedInputStream(connection.getInputStream());
        response = StreamToString(in);

        return response;
    }

}
