package com.agamilabs.smartshop.model;

public class PrintersInfo {
    private String printerName;
    private String printerAddress;

    public PrintersInfo(String printerName, String printerAddress) {
        this.printerName = printerName;
        this.printerAddress = printerAddress;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public String getPrinterAddress() {
        return printerAddress;
    }

    public void setPrinterAddress(String printerAddress) {
        this.printerAddress = printerAddress;
    }
}
