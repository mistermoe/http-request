package com.mistermoe.httprequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class GenericResponseHandler implements ResponseHandler {
    @Override
    public void handle(HttpURLConnection connection, HTTPResponse response) throws Exception {

        BufferedReader responseReader;
        StringBuilder responseBuilder = new StringBuilder();

        try {
            connection.connect();
            response.status(connection.getResponseCode());

            responseReader = new BufferedReader(
                new InputStreamReader(connection.getInputStream())
            );

            String line;
            while ((line = responseReader.readLine()) != null) {
                responseBuilder.append(line);
                responseBuilder.append("\n");
            }

            if (responseBuilder.length() > 0) {
                responseBuilder.deleteCharAt(responseBuilder.length() - 1);
                response.body(responseBuilder.toString());
            }

            responseReader.close();
            connection.getInputStream().close();
        }
        catch (IOException e) {
            response.status(connection.getResponseCode());

            InputStream errorStream = connection.getErrorStream();
            if (errorStream != null) {
                responseReader = new BufferedReader(new InputStreamReader(errorStream));
                String line;
                while ((line = responseReader.readLine()) != null) {
                    responseBuilder.append(line);
                    responseBuilder.append("\n");
                }

                responseBuilder.deleteCharAt(responseBuilder.length() - 1);
                response.body(responseBuilder.toString());
            }

            throw new HTTPRequestException("unable to handle response", e, response);
        }

    }
}
