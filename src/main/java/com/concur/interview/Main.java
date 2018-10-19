package com.concur.interview;
import com.concur.interview.RequestHandler.Request;
import com.google.gson.*;
import io.javalin.Context;
import io.javalin.Handler;
import io.javalin.Javalin;
import java.util.List;

public class Main {

    private static RequestHandler requestHandler = new RequestHandler();

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        app.post("/items", new PostItemsHandler());
        app.get("/items", new GetItemsHandler());
    }


    private static class PostItemsHandler implements Handler {

        public void handle(Context ctx) {
            String bodyResult = ctx.body();
            try {
                Request requestBody = parseRequestBody(bodyResult);
                requestHandler.addRequest(requestBody);
                ctx.status(201);
            } catch (JsonParseException e) {
                ctx.status(400);
            }
        }

        private Request parseRequestBody(String requestBody) {
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(requestBody, Request.class);
        }
    }

    private static class GetItemsHandler implements Handler {
        public void handle(Context ctx) {
            try {
                List<Request> response = chooseResponse();
                ctx.result(response.toString());
                ctx.status(200);
            } catch (Exception e) {
                System.out.println(e);
                ctx.status(400);
            }
        }

        private List<Request> chooseResponse() {
            List<Request> lastTwoSecondsResponse = requestHandler.getLastTwoSecondsRequests();
            List<Request> lastHundredRequests = requestHandler.getLastHundredRequests();
            if (lastTwoSecondsResponse.size() > lastHundredRequests.size()) {
                return lastTwoSecondsResponse;
            }
            return lastHundredRequests;
        }
    }
}