package com.agamilabs.smartshop.ui.storeadmin.models;

public class StoreTopCustomerModel {
    private int userNo, storeNo, totalCarts;
    private double totalAmount;
    private String firstName, lastName, contactNo, imageUrl;

    public StoreTopCustomerModel(int userNo, int storeNo, int totalCarts, double totalAmount, String firstName, String lastName, String contactNo, String imageUrl) {
        this.userNo = userNo;
        this.storeNo = storeNo;
        this.totalCarts = totalCarts;
        this.totalAmount = totalAmount;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNo = contactNo;
        this.imageUrl = imageUrl;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public int getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(int storeNo) {
        this.storeNo = storeNo;
    }

    public int getTotalCarts() {
        return totalCarts;
    }

    public void setTotalCarts(int totalCarts) {
        this.totalCarts = totalCarts;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
