package com.agamilabs.smartshop.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.agamilabs.smartshop.model.InvoiceItem;
import com.agamilabs.smartshop.model.InvoiceModel;


import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartShop_db";
    private static final String TABLE_1 = "Selected_product_list_table";
    private static final String TABLE_2 = "Invoice_view_table";
    private static final int DATABASE_VERSION = 1;

    private static final String dID = "_id";
    private static final String dProductID = "ProductID";
    private static final String dProductName = "Product_name";
    private static final String dCustomer = "customer";
    private static final String dDate = "date";
    private static final String dProductQuantity = "Product_quantity";
    private static final String dProductPrice = "Product_price";
    private static final String dDiscount = "discount";
    private static final String dTotalBill = "total_bill";
    //    private static final String dProductImgUrl = "Product_imgUrl";
    private static final String dID2 = "_id";
    private static final String dInvoiceNO = "invoice_no";
    private static final String dCustomerName = "customer_name";
    private static final String dCurrentDate = "date";
    private static final String dDueDate = "due_date";
    private static final String dAmount = "amount";
    private static final String dDiscount2 = "discount2";
    private static final String dDeduction = "deduction";
    private static final String dPaid = "paid";
    private static final String dStatus = "status";
    private static final String dAction = "action_attr";


    private static final String dCREATE_TABLE = "CREATE TABLE " + TABLE_1 + " (" + dID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + dProductID + " VARCHAR(255),  " + dProductName + " VARCHAR(255), " +
            "" + dProductQuantity + " VARCHAR(255)," + dProductPrice + " VARCHAR(255), " + dTotalBill + " VARCHAR(255))";

    private static final String dCREATE_TABLE2 = "CREATE TABLE " + TABLE_2 + " (" + dID2 + " INTEGER PRIMARY KEY AUTOINCREMENT , " + dInvoiceNO + " TEXT, " + dCustomerName + " VARCHAR(255)," +
            "" + dCurrentDate + " TEXT, " + dDueDate + " TEXT," + dDiscount2 + " TEXT," + dDeduction + " TEXT, " + dAmount + " TEXT, " + dPaid + " VARCHAR(255), " + dStatus + " VARCHAR(255), " + dAction + " VARCHAR(255))";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(dCREATE_TABLE);
            db.execSQL(dCREATE_TABLE2);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_1);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_2);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }


    public long mAddSelectedProduct(String productID, String productName, String productQuantity, String product_price, String totalBill) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(dProductID, productID);
        contentValues.put(dProductName, productName);
        contentValues.put(dProductQuantity, productQuantity);
        contentValues.put(dProductPrice, product_price);
        contentValues.put(dTotalBill, totalBill);
//        contentValues.put(dProductImgUrl, productImgUrl);

        long id = sqLiteDatabase.insert(TABLE_1, null, contentValues);
        return id;
    }

    public long mAddInvoiceData(String InvoiceNO, String CustomerName, String CurrentDate, String DueDate, String Discount2, String Deduction, String Amount, String Paid, String Status, String Action) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(dInvoiceNO, InvoiceNO);
        contentValues.put(dCustomerName, CustomerName);
        contentValues.put(dCurrentDate, CurrentDate);
        contentValues.put(dDueDate, DueDate);
        contentValues.put(dDiscount2, Discount2);
        contentValues.put(dDeduction, Deduction);
        contentValues.put(dAmount, Amount);
        contentValues.put(dPaid, Paid);
        contentValues.put(dStatus, Status);
        contentValues.put(dAction, Action);

        long id = db.insert(TABLE_2, null, contentValues);
        return id;
    }

    public List<InvoiceModel> showAllInvoiceData() {
        String sql = "SELECT * from " + TABLE_2;
        SQLiteDatabase db = this.getReadableDatabase();
        List<InvoiceModel> storeData = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                String invoiceNo = cursor.getString(cursor.getColumnIndex(dInvoiceNO));
                String customerName = cursor.getString(cursor.getColumnIndex(dCustomerName));
                String date = cursor.getString(cursor.getColumnIndex(dCurrentDate));
                String dueDate = cursor.getString(cursor.getColumnIndex(dDueDate));
                String discount2 = cursor.getString(cursor.getColumnIndex(dDiscount2));
                String deduction = cursor.getString(cursor.getColumnIndex(dDeduction));
                String amount = cursor.getString(cursor.getColumnIndex(dAmount));
                String paid = cursor.getString(cursor.getColumnIndex(dPaid));
                String status = cursor.getString(cursor.getColumnIndex(dStatus));
                String action = cursor.getString(cursor.getColumnIndex(dAction));

                storeData.add(new InvoiceModel(invoiceNo, customerName, date, dueDate, Double.parseDouble(discount2), Double.parseDouble(deduction), Double.parseDouble(amount)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return storeData;
    }

    public List<InvoiceItem> showSelectedProductsInScannerActivity() {
        String sql = "select Product_name, Product_price, Product_quantity, total_bill from " + TABLE_1;
        SQLiteDatabase db = this.getReadableDatabase();
        List<InvoiceItem> storeProductDetails = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {

            do {
                String productName = cursor.getString(cursor.getColumnIndex(dProductName));
                String productPrice = cursor.getString(cursor.getColumnIndex(dProductPrice));
                String productQuantity = cursor.getString(cursor.getColumnIndex(dProductQuantity));
                String totalBill = cursor.getString(cursor.getColumnIndex(dTotalBill));

                storeProductDetails.add(new InvoiceItem(productName, Double.parseDouble(productQuantity), Double.parseDouble(productPrice), Double.parseDouble(totalBill)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return storeProductDetails;
    }

    public List<InvoiceItem> invoiceItemTable() {
        String sql = "select Product_name, Product_price, Product_quantity, total_bill from " + TABLE_1;
        SQLiteDatabase db = this.getReadableDatabase();
        List<InvoiceItem> storeData = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(dProductName));
                String price = cursor.getString(cursor.getColumnIndex(dProductPrice));
                String qty = cursor.getString(cursor.getColumnIndex(dProductQuantity));
                String total = cursor.getString(cursor.getColumnIndex(dTotalBill));
                storeData.add(new InvoiceItem(name, Double.parseDouble(qty), Double.parseDouble(price), Double.parseDouble(total)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return storeData;

    }

    public ArrayList<InvoiceItem> showAllSelectedProductsInInvoiceActivity() {
        String sql = "select * from " + TABLE_1;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<InvoiceItem> storeProductDetails = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {

            do {
                String customer = cursor.getString(1);
                String date = cursor.getString(2);
                String productName = cursor.getString(3);
                String productQuantity = cursor.getString(4);
                String productPrice = cursor.getString(5);
                String discount = cursor.getString(6);
                String totalBill = cursor.getString(7);

//                storeProductDetails.add(new InvoiceItem(customer, date, productName, Double.parseDouble(productQuantity), Double.parseDouble(productPrice), Double.parseDouble(discount), Double.parseDouble(totalBill)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return storeProductDetails;
    }

    public void mDeleteProductList() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_1);

        } catch (SQLiteException e){
            e.printStackTrace();
        }

    }
}
