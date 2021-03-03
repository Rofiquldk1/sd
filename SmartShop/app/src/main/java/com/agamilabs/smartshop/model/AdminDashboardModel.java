package com.agamilabs.smartshop.model;

import java.lang.reflect.Field;

public class AdminDashboardModel {

    public String section_id;
    public String section_title;
    public String section_identifier ;

    public Field[] getAllFields(){
        return this.getClass().getDeclaredFields() ;
    }

    public AdminDashboardModel() {
    }

    public String getSection_id() {
        return section_id;
    }

    public String getSection_title() {
        return section_title;
    }
    public String getSection_identifier() {
        return section_identifier;
    }

}
