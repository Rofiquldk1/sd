package com.agamilabs.smartshop.model;

public class Sms {
    public String campaignName;
    public String recipientsNumber;
    public String message;
    public String messageDate;
    public String messageTime;
    public String messageStatus;
    public String apiType;

    public Sms(String campaignName, String recipientsNumber, String message, String messageDate, String messageTime, String messageStatus, String apiType) {
        this.campaignName = campaignName;
        this.recipientsNumber = recipientsNumber;
        this.message = message;
        this.messageDate = messageDate;
        this.messageTime = messageTime;
        this.messageStatus = messageStatus;
        this.apiType = apiType;
    }
}
