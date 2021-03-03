package com.agamilabs.smartshop.model;

import java.io.Serializable;
import java.util.ArrayList;

public class InvoiceModel implements Serializable{
    String invoiceNo;
    String customer;
    String currentDate, dueDate;
    String currency = "BDT";

    ArrayList<InvoiceItem> items = new ArrayList<>();

    double discount;
    double deduction;
    double amount;


    public InvoiceModel(String invoiceNo, String customer, String currentDate, String dueDate, double discount, double deduction, double amount) {
        this.invoiceNo = invoiceNo;
        this.customer = customer;
        this.currentDate = currentDate;
        this.dueDate = dueDate;
        this.discount = discount;
        this.deduction = deduction;
        this.amount = amount;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public String getCustomer() {
        return customer;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getCurrency() {
        return currency;
    }

    public ArrayList<InvoiceItem> getItems() {
        return items;
    }

    public double getDiscount() {
        return discount;
    }

    public double getDeduction() {
        return deduction;
    }
}
