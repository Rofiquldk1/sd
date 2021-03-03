package com.agamilabs.smartshop.model;

public class NotifyModel {

    private String id;
    private String title;
    private String body_text;
    private String topic;


    public NotifyModel() {
    }

    public NotifyModel(String id, String title, String body_text, String topic) {
        this.id = id;
        this.title = title;
        this.body_text = body_text;
        this.topic = topic;
    }

    public NotifyModel(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody_text() {
        return body_text;
    }

    public void setBody_text(String body_text) {
        this.body_text = body_text;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
