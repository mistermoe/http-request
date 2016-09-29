package com.mistermoe.httprequest;


import static spark.Spark.*;
import static spark.Spark.stop;

public class Server {

    public static void start() {
        get("/200", (req, res) -> {
            res.status(200);
            return "";
        });

        get("/400", (req, res) -> {
           res.status(400);
            return "berry berry bayd.";
        });

        post("/echo-request-body", (req, res) -> {
            res.status(200);
            return req.body();
        });
    }

    public static void kill() {
        System.out.println("death from above");
        stop();
    }
}
