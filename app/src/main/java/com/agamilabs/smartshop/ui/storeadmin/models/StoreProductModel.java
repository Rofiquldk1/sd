package com.agamilabs.smartshop.ui.storeadmin.models;

public class StoreProductModel {
    private int productNo, storeProductNo, categoryNo, unitId, sellRound, minOrderQty, maxOrderQty, availability;
    private double rate, discountRate, unitQty;
    private String productName, brandName, systemImageUrl, storeImageUrl, unitTextShort, unitText;

    public StoreProductModel(int productNo, int storeProductNo, int categoryNo, int unitId, int sellRound, int minOrderQty, int maxOrderQty, int availability, double rate, double discountRate, double unitQty, String productName, String brandName, String systemImageUrl, String storeImageUrl, String unitTextShort, String unitText) {
        this.productNo = productNo;
        this.storeProductNo = storeProductNo;
        this.categoryNo = categoryNo;
        this.unitId = unitId;
        this.sellRound = sellRound;
        this.minOrderQty = minOrderQty;
        this.maxOrderQty = maxOrderQty;
        this.availability = availability;
        this.rate = rate;
        this.discountRate = discountRate;
        this.unitQty = unitQty;
        this.productName = productName;
        this.brandName = brandName;
        this.systemImageUrl = systemImageUrl;
        this.storeImageUrl = storeImageUrl;
        this.unitTextShort = unitTextShort;
        this.unitText = unitText;
    }

    public StoreProductModel(int productNo, int storeProductNo, int unitId, double rate, double unitQty, String productName, String systemImageUrl, String storeImageUrl, String unitTextShort) {
        this.productNo = productNo;
        this.storeProductNo = storeProductNo;
        this.unitId = unitId;
        this.rate = rate;
        this.unitQty = unitQty;
        this.productName = productName;
        this.systemImageUrl = systemImageUrl;
        this.storeImageUrl = storeImageUrl;
        this.unitTextShort = unitTextShort;
    }

    public int getProductNo() {
        return productNo;
    }

    public void setProductNo(int productNo) {
        this.productNo = productNo;
    }

    public int getStoreProductNo() {
        return storeProductNo;
    }

    public void setStoreProductNo(int storeProductNo) {
        this.storeProductNo = storeProductNo;
    }

    public int getCategoryNo() {
        return categoryNo;
    }

    public void setCategoryNo(int categoryNo) {
        this.categoryNo = categoryNo;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public int getSellRound() {
        return sellRound;
    }

    public void setSellRound(int sellRound) {
        this.sellRound = sellRound;
    }

    public int getMinOrderQty() {
        return minOrderQty;
    }

    public void setMinOrderQty(int minOrderQty) {
        this.minOrderQty = minOrderQty;
    }

    public int getMaxOrderQty() {
        return maxOrderQty;
    }

    public void setMaxOrderQty(int maxOrderQty) {
        this.maxOrderQty = maxOrderQty;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public double getUnitQty() {
        return unitQty;
    }

    public void setUnitQty(double unitQty) {
        this.unitQty = unitQty;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSystemImageUrl() {
        return systemImageUrl;
    }

    public void setSystemImageUrl(String systemImageUrl) {
        this.systemImageUrl = systemImageUrl;
    }

    public String getStoreImageUrl() {
        return storeImageUrl;
    }

    public void setStoreImageUrl(String storeImageUrl) {
        this.storeImageUrl = storeImageUrl;
    }

    public String getUnitTextShort() {
        return unitTextShort;
    }

    public void setUnitTextShort(String unitTextShort) {
        this.unitTextShort = unitTextShort;
    }

    public String getUnitText() {
        return unitText;
    }

    public void setUnitText(String unitText) {
        this.unitText = unitText;
    }
}
