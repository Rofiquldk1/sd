package com.agamilabs.smartshop.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class OrderReportModel {

    public String cartno, storeno, userno, ulat, ulon;
    public  String forstreet, forcity, forpostcode, forcontact, forlat, forlon ;
    public String cartdatetime, delivarydatetime, customerdeliverytypechoice, cartorderid, pimage ;
    public String  postalstreet, postalcity, ufirstname, ulastname, statusno ;
    public String  statustime, ratingcount, review_message, rrcatno ;

    public List<CartStatusModel> mCartStatusList = new ArrayList<>() ;

    public OrderReportModel() {
    }

    public Field[] getAllFields(){
        return this.getClass().getDeclaredFields() ;
    }


    public OrderReportModel(String forstreet, String forcity, String forpostcode, String forcontact, String cartdatetime,
                            String delivarydatetime, String cartorderid, String ufirstname, String ulastname, List<CartStatusModel> mCartStatusList) {
        this.forstreet = forstreet;
        this.forcity = forcity;
        this.forpostcode = forpostcode;
        this.forcontact = forcontact;
        this.cartdatetime = cartdatetime;
        this.delivarydatetime = delivarydatetime;
        this.cartorderid = cartorderid;
        this.ufirstname = ufirstname;
        this.ulastname = ulastname;
        this.mCartStatusList = mCartStatusList;
    }

    public String getCartno() {
        return cartno;
    }

    public String getStoreno() {
        return storeno;
    }

    public String getUserno() {
        return userno;
    }

    public String getUlat() {
        return ulat;
    }

    public String getUlon() {
        return ulon;
    }

    public String getForstreet() {
        return forstreet;
    }

    public String getForcity() {
        return forcity;
    }

    public String getForpostcode() {
        return forpostcode;
    }

    public String getForcontact() {
        return forcontact;
    }

    public String getForlat() {
        return forlat;
    }

    public String getForlon() {
        return forlon;
    }

    public String getCartdatetime() {
        return cartdatetime;
    }

    public String getDelivarydatetime() {
        return delivarydatetime;
    }

    public String getCustomerdeliverytypechoice() {
        return customerdeliverytypechoice;
    }

    public String getCartorderid() {
        return cartorderid;
    }

    public String getPimage() {
        return pimage;
    }

    public String getPostalstreet() {
        return postalstreet;
    }

    public String getPostalcity() {
        return postalcity;
    }

    public String getUfirstname() {
        return ufirstname;
    }

    public String getUlastname() {
        return ulastname;
    }

    public String getStatusno() {
        return statusno;
    }

    public String getStatustime() {
        return statustime;
    }

    public String getRatingcount() {
        return ratingcount;
    }

    public String getReview_message() {
        return review_message;
    }

    public String getRrcatno() {
        return rrcatno;
    }

    public List<CartStatusModel> getmCartStatusList() {
        return mCartStatusList;
    }

    @Override
    public String toString() {
        return "OrderReportModel{" +

                ", forstreet='" + forstreet + '\'' +"\n"+

                ", mCartStatusList=" + mCartStatusList +"\n"+
                '}';
    }
}
