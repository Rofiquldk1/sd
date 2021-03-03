package com.agamilabs.smartshop.model;

public class CampaignItem {

    public String schedule_no;
    public String message_title;
    public String message;
    public String catid;
    public String session;
    public String apiid;
    public String schedule_starting_datetime;
    public String schedule_particular_number;
    public String priority;
    public String status;
    public String schedule_entry_datetime;
    public String total_subscribers;
    public String total_sent_subscribers;
    //public String total_cost;


    public CampaignItem() {

    }

    public CampaignItem(String schedule_no, String message_title, String message, String catid, String session, String apiid, String schedule_starting_datetime, String schedule_particular_number, String priority, String status, String schedule_entry_datetime, String total_subscribers, String total_sent_subscribers) {
        this.schedule_no = schedule_no;
        this.message_title = message_title;
        this.message = message;
        this.catid = catid;
        this.session = session;
        this.apiid = apiid;
        this.schedule_starting_datetime = schedule_starting_datetime;
        this.schedule_particular_number = schedule_particular_number;
        this.priority = priority;
        this.status = status;
        this.schedule_entry_datetime = schedule_entry_datetime;
        this.total_subscribers = total_subscribers;
        this.total_sent_subscribers = total_sent_subscribers;
    }

    public String getSchedule_no() {
        return schedule_no;
    }

    public void setSchedule_no(String schedule_no) {
        this.schedule_no = schedule_no;
    }

    public String getMessage_title() {
        return message_title;
    }

    public void setMessage_title(String message_title) {
        this.message_title = message_title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getApiid() {
        return apiid;
    }

    public void setApiid(String apiid) {
        this.apiid = apiid;
    }

    public String getSchedule_starting_datetime() {
        return schedule_starting_datetime;
    }

    public void setSchedule_starting_datetime(String schedule_starting_datetime) {
        this.schedule_starting_datetime = schedule_starting_datetime;
    }

    public String getSchedule_particular_number() {
        return schedule_particular_number;
    }

    public void setSchedule_particular_number(String schedule_particular_number) {
        this.schedule_particular_number = schedule_particular_number;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSchedule_entry_datetime() {
        return schedule_entry_datetime;
    }

    public void setSchedule_entry_datetime(String schedule_entry_datetime) {
        this.schedule_entry_datetime = schedule_entry_datetime;
    }

    public String getTotal_subscribers() {
        return total_subscribers;
    }

    public void setTotal_subscribers(String total_subscribers) {
        this.total_subscribers = total_subscribers;
    }

    public String getTotal_sent_subscribers() {
        return total_sent_subscribers;
    }

    public void setTotal_sent_subscribers(String total_sent_subscribers) {
        this.total_sent_subscribers = total_sent_subscribers;
    }

}
