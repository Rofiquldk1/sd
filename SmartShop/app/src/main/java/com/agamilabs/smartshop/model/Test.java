package com.agamilabs.smartshop.model;

public class Test {
    public String SmsID;
    public String recipientsNumber;
    public String message;
    public String messageDateTime;

    public Test(String smsID, String recipientsNumber, String message, String messageDateTime) {
        SmsID = smsID;
        this.recipientsNumber = recipientsNumber;
        this.message = message;
        this.messageDateTime = messageDateTime;
    }
}
