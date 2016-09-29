package com.mistermoe.httprequest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class HTTPRequestTests {
    public static String testServerURL = "http://localhost:4567";

    @BeforeClass
    public static void beforeAll() throws InterruptedException {
        Server.start();
        Thread.currentThread().sleep(1000); // give the server time to start
    }

    @Test
    public void test1() throws MalformedURLException {
        System.out.println(".get() can handle a simple request");
        HTTPResponse response = new HTTPRequest(testServerURL + "/200").get();
        assertEquals(response.status(), 200);
    }

    @Test
    public void test2() {
        System.out.println(".get() throws an HTTPRequestException for a 4xx response");
        try {
            HTTPResponse response = new HTTPRequest(testServerURL + "/400").get();
        }
        catch (HTTPRequestException e) {
            assertEquals(e.response.status(), 400);
            assertEquals(e.response.body(), "berry berry bayd.");
        }
    }

    @Test
    public void test3() {
        System.out.println(".post() sends request body to server if one is provided");

        String requestBody = "this is a request body";
        HTTPResponse response =
            new HTTPRequest(testServerURL + "/echo-request-body")
                .setBody(requestBody)
                .post();

        assertEquals(response.body(), requestBody);
    }

    @AfterClass
    public static void afterAll() {
        Server.kill();
    }
}
