package com.agamilabs.smartshop.model;

public class ScheduleItem {
    public String campaignName;
    public String recipients;
    public String campaignMessage;
    public String campaignStartDateTime;
    public String notificationOn;
    public String notifyTimeBeforeMesaging;
    public String apiType;
    public String saveAsTemplate;

    public ScheduleItem() {

    }

    public ScheduleItem(String campaignName, String recipients, String campaignMessage, String campaignStartDateTime, String notificationOn, String notifyTimeBeforeMesaging, String apiType, String saveAsTemplate) {
        this.campaignName = campaignName;
        this.recipients = recipients;
        this.campaignMessage = campaignMessage;
        this.campaignStartDateTime = campaignStartDateTime;
        this.notificationOn = notificationOn;
        this.notifyTimeBeforeMesaging = notifyTimeBeforeMesaging;
        this.apiType = apiType;
        this.saveAsTemplate = saveAsTemplate;
    }
}
