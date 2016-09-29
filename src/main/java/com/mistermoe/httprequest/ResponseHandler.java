package com.mistermoe.httprequest;

import java.net.HttpURLConnection;

@FunctionalInterface
public interface ResponseHandler {
    void handle(HttpURLConnection connection , HTTPResponse response) throws Exception;
}
