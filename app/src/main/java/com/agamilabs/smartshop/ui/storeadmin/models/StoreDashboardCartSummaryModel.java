package com.agamilabs.smartshop.ui.storeadmin.models;

public class StoreDashboardCartSummaryModel {
    private int totalCarts;
    private double totalAmounts;
    private String summaryItemTitle;

    public StoreDashboardCartSummaryModel(int totalCarts, double totalAmounts, String summaryItemTitle) {
        this.totalCarts = totalCarts;
        this.totalAmounts = totalAmounts;
        this.summaryItemTitle = summaryItemTitle;
    }

    public int getTotalCarts() {
        return totalCarts;
    }

    public void setTotalCarts(int totalCarts) {
        this.totalCarts = totalCarts;
    }

    public double getTotalAmounts() {
        return totalAmounts;
    }

    public void setTotalAmounts(double totalAmounts) {
        this.totalAmounts = totalAmounts;
    }

    public String getSummaryItemTitle() {
        return summaryItemTitle;
    }

    public void setSummaryItemTitle(String summaryItemTitle) {
        this.summaryItemTitle = summaryItemTitle;
    }
}
