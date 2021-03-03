package com.agamilabs.smartshop.model;

import java.lang.reflect.Field;

public class CartStatusModel {

    public String cstatusno, statustitle, icon, is_end, statusno, passed ;


    public CartStatusModel() {
    }

    public CartStatusModel(String cstatusno, String statustitle, String icon, String is_end, String statusno, String passed) {
        this.cstatusno = cstatusno;
        this.statustitle = statustitle;
        this.icon = icon;
        this.is_end = is_end;
        this.statusno = statusno;
        this.passed = passed;
    }

    public Field[] getAllFields(){
        return this.getClass().getDeclaredFields() ;
    }

    public String getCstatusno() {
        return cstatusno;
    }

    public String getStatustitle() {
        return statustitle;
    }

    public String getIcon() {
        return icon;
    }

    public String getIs_end() {
        return is_end;
    }

    public String getStatusno() {
        return statusno;
    }

    public String getPassed() {
        return passed;
    }


    @Override
    public String toString() {
        return "CartStatusModel{" +
                "cstatusno='" + cstatusno + '\'' +"\n"+
                ", statustitle='" + statustitle + '\'' +"\n"+
                ", icon='" + icon + '\'' +"\n"+
                ", is_end='" + is_end + '\'' +"\n"+
                ", statusno='" + statusno + '\'' +"\n"+
                ", passed='" + passed + '\'' +"\n"+
                '}';
    }
}