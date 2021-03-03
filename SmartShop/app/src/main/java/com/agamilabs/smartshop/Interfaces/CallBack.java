package com.agamilabs.smartshop.Interfaces;

public interface CallBack {
    void onClickButton(int position,String scheduleNo);
    void duplicateInfo(String schedule_no, String message_title, String message, String catid, String session, String apiid, String schedule_starting_datetime, String schedule_particular_number, String priority, String status, String schedule_entry_datetime,
                       String total_subscribers, String total_sent_subscribers);
}