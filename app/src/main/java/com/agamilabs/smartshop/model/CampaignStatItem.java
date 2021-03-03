package com.agamilabs.smartshop.model;

public class CampaignStatItem {
    public String campaign;
    public String message;
    public String total_campaign;
    public String total_message;

    public CampaignStatItem() {

    }

    public CampaignStatItem(String campaign, String message, String total_campaign, String total_message) {
        this.campaign = campaign;
        this.message = message;
        this.total_campaign = total_campaign;
        this.total_message = total_message;
    }

    public String getCampaign() {
        return this.campaign;
    }

    public String getMessage() {
        return this.message;
    }

    public String getTotal_campaign() {
        return this.total_campaign;
    }

    public String getTotal_message() {
        return this.total_message;
    }
}
