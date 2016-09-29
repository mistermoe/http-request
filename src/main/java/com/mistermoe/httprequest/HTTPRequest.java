package com.mistermoe.httprequest;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class HTTPRequest {
    private URL url;
    private Map<String, String> headers;
    private String body;
    private boolean followRedirects = true;

    public HTTPRequest(String url) throws HTTPRequestException {
        try {
            this.url = new URL(url);
        }
        catch (MalformedURLException e) {
            throw new HTTPRequestException("malformed url", e);
        }
        this.headers = new HashMap<>();
    }

    /**
     * adds a request header
     * @param key - the name of the header
     * @param value - the value of the header
     * @return HTTPRequest
     */
    public HTTPRequest addHeader(String key, String value) {
        this.headers.put(key, value);

        return this;
    }

    /**
     * sets request headers
     * @param headers - header names mapped to their respective values
     * @return HTTPRequest - the request being built
     */
    public HTTPRequest setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    /**
     * sets the request body
     * @param body
     * @return HTTPRequest - the request being built
     */
    public HTTPRequest setBody(String body) {
        this.body = body;
        return this;
    }

    /**
     * sets whether redirects should be followed or not (defaults to true)
     * @param follow - a boolean indicating whether or not to follow redirects
     * @return HTTPRequest - the request being built
     */
    public HTTPRequest followRedirects(boolean follow) {
        this.followRedirects = follow;
        return this;
    }

    /**
     * base64 encodes the provided credentials and sets the appropriate request header
     * @param username - username
     * @param password - password
     * @return HTTPRequest - the request being built
     */
    public HTTPRequest setBasicAuth(String username, String password) {
        this.setBasicAuth(username + ":" + password);
        return this;
    }

    /**
     * base64 encodes the provided credentials and sets the appropriate request header
     * @param authentication - the user provided authentication
     * @return HTTPRequest - the request being built
     */
    public HTTPRequest setBasicAuth(String authentication) {

        if (!authentication.contains(":")) authentication += ":";

        String base64EncodedCredentials = base64EncodeUserInfo(authentication);
        this.headers.put("Authorization", "Basic " + base64EncodedCredentials);

        return this;
    }

    /**
     * utility method that base64 encodes the provided string and returns it
     * @param str - the string to be encoded
     * @return encodedStr - the encoded string
     */
    private static String base64EncodeUserInfo(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    /**
     * executes a get request on the url provided
     * @return HTTPResponse response
     * @throws HTTPRequestException
     */
    public HTTPResponse get() throws HTTPRequestException {

        HttpURLConnection connection = this.createConnection("GET");
        HTTPResponse response = new HTTPResponse();

        request(connection, response, new GenericResponseHandler());

        return response;
    }

    public HTTPResponse get(ResponseHandler handler) {
        return null;
    }

    private void request(HttpURLConnection connection, HTTPResponse response, ResponseHandler handler) throws HTTPRequestException {
        try {
            handler.handle(connection, response);
        }
        catch (HTTPRequestException e) {
            throw e;
        }
        catch (Exception e) {
            throw new HTTPRequestException("unable to handle response", e);
        }
    }

    /**
     * executes a post request on the url provided
     * @return HTTPResponse response
     * @throws HTTPRequestException
     */
    public HTTPResponse post() throws HTTPRequestException {

        HttpURLConnection connection = this.createConnection("POST");
        HTTPResponse response = new HTTPResponse();

        request(connection, response, new GenericResponseHandler());

        return response;
    }

    private HttpURLConnection createConnection(String method) throws HTTPRequestException {
        HttpURLConnection connection;
        String userInfo = this.url.getUserInfo();
        if (userInfo != null && userInfo.length() > 0) this.setBasicAuth(userInfo);

        try {
            if (this.url.getProtocol().equals("https"))
                connection = (HttpsURLConnection) this.url.openConnection();
            else
                connection = (HttpURLConnection) this.url.openConnection();

            connection.setRequestMethod(method);

            if (this.body != null) {
                byte[] requestBodyBytes = this.body.getBytes("UTF-8");

                connection.setDoOutput(true);
                connection.setRequestProperty(
                    "Content-Length",
                    Integer.toString(requestBodyBytes.length)
                );

                connection.getOutputStream().write(this.body.getBytes("UTF-8"));
            }
        }
        catch (IOException e) {
            throw new HTTPRequestException("unable to open connection", e);

        }

        this.headers.forEach((key, value) -> connection.addRequestProperty(key, value));
        connection.setInstanceFollowRedirects(this.followRedirects);

        return connection;
    }
}