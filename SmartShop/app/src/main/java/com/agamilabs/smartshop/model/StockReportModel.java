package com.agamilabs.smartshop.model;

import java.lang.reflect.Field;

public class StockReportModel {

   public String itemno;
    public  String itemname;
    public String  initialqty, remainingqty, salerate, prate, stockamount ;
    public String stockinqty, stockoutqty ;
    public String reorderpoint ;

    public StockReportModel() {
    }

    public Field[] getAllFields(){
        return this.getClass().getDeclaredFields() ;
    }


    public String getItemno() {
        return itemno;
    }

    public String getItemname() {
        return itemname;
    }

    public String getReorderpoint() {
        return reorderpoint;
    }

    public String getInitialqty() {
        return initialqty;
    }

    public String getRemainingqty() {
        return remainingqty;
    }

    public String getSalerate() {
        return salerate;
    }

    public String getPrate() {
        return prate;
    }

    public String getStockamount() {
        return stockamount;
    }

    public String getStockinqty() {
        return stockinqty;
    }

    public String getStockoutqty() {
        return stockoutqty;
    }


    @Override
    public String toString() {
        return "StockReportModel{" +
                "itemno=" + itemno +
                ", reorderpoint=" + reorderpoint +
                ", initialqty=" + initialqty +
                ", remainingqty=" + remainingqty +
                ", salerate=" + salerate +
                ", prate=" + prate +
                ", stockamount=" + stockamount +
                ", itemname='" + itemname + '\'' +
                ", stockinqty=" + stockinqty +
                ", stockoutqty=" + stockoutqty +
                '}';
    }
}
