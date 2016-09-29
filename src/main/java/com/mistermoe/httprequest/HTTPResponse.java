package com.mistermoe.httprequest;


public class HTTPResponse {
    private int statusCode;
    private String body;

    public HTTPResponse() {}

    public void status(int statusCode) {
        this.statusCode = statusCode;
    }

    public int status() {
        return this.statusCode;
    }

    public void body(String body) {
        this.body = body;
    }

    public String body() {
        return this.body;
    }
}
