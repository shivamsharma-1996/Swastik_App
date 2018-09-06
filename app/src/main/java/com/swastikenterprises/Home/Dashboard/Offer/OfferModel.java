package com.swastikenterprises.Home.Dashboard.Offer;

public class OfferModel
{
    public String title;
    public String body;
    public Long timestamp;

    public OfferModel() {
    }

    public OfferModel(String title, String body, Long timestamp) {
        this.title = title;
        this.body = body;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
