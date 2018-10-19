package com.concur.interview;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

class RequestHandler {

    private List<Request> requests = new ArrayList<>();

    List<Request> getRequest() {
        return this.requests;
    }

    void setRequest(List<Request> requests) {
        this.requests = requests;
    }

    void addRequest(Request request) {
        this.requests.add(request);
    }

    void removeRequest(Request request) {
        this.requests.remove(request);
    }

    List<Request> getLastHundredRequests() {
        if (this.requests.size() >= 100) {
            return this.requests.subList(this.requests.size()-100, this.requests.size());
        }
        return this.requests;
    }

    List<Request> getLastTwoSecondsRequests() {
        Date now = new Date();
        ArrayList<Request> lastTwoSecondsRequests = new ArrayList<>();
        for (Request request : this.requests) {
            long diffInDays = now.getTime() - request.getItem().getTimestamp().getTime();
            int diffInSeconds = (int) TimeUnit.DAYS.convert(diffInDays, TimeUnit.SECONDS);
            if (diffInSeconds <= 2) {
                lastTwoSecondsRequests.add(request);
            }
        }
        return lastTwoSecondsRequests;
    }

    private static class Item {

        private Date timestamp;
        private Integer id;

        void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }

        Date getTimestamp() {
            return this.timestamp;
        }

        void setId(Integer id) {
            this.id = id;
        }

        Integer getId() {
            return this.id;
        }

        @Override
        public String toString() {
            return new com.google.gson.Gson().toJson(this);
        }
    }

    public static class Request {
        private Item item;

        void setItem(Item item) {
            this.item = item;
        }

        Item getItem() {
            return this.item;
        }

        @Override
        public String toString() {
            return new com.google.gson.Gson().toJson(this);
        }
    }

}
