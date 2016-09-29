package com.mistermoe.httprequest;

public class HTTPRequestException extends RuntimeException {
    HTTPResponse response;
    public HTTPRequestException(String message) {
        super(message);
    }

    public HTTPRequestException(Throwable cause) {
        super(cause);
    }

    public HTTPRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public HTTPRequestException(String message, Throwable cause, HTTPResponse response) {
        super(message, cause);
        this.response = response;
    }

    public HTTPRequestException(String message, Throwable cause, boolean enableSupression, boolean writableStacktrace) {
        super(message, cause, enableSupression, writableStacktrace);
    }
}
