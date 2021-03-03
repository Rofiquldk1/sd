package com.agamilabs.smartshop.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.agamilabs.smartshop.model.Sms;
import com.agamilabs.smartshop.model.Test;

import java.util.ArrayList;

public class SmsDatabaseHelper extends SQLiteOpenHelper {
    // Initialise constants
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SMS";
    private static final String SMS_TABLE_NAME = "Messages";
    private static final String TEST_TABLE_NAME = "SmsObserver";
    //private static final String SMSQueue_TABLE_NAME = "HandelSMSQueue";
    private static final String APPOBSERVER_TABLE_NAME = "AppObserver";
    //private static final String CLIENT_STATUS_TABLE_NAME = "ClientStatus";
    private static final String[] COLUMN_NAMES = {"SmsID", "CampaignName", "RecipientsNumber","Message",
            "MessageDate", "MessageTime","MessageStatus","APIType"};
    private static final String[] COLUMN_NAMES_TEST = {"SmsID","RecipientsNumber","Message", "MessageDateTime","SQLiteDeafultDateTime"};
    //private static final String[] COLUMN_NAMES_SMSQueue = {"SLNO","CampaignScheduleDateTime"};
    private static final String[] COLUMN_NAMES_APPOBSERVER = {"Action","CalenderDateTime","SQLiteDeafultDateTime"};
    //private static final String[] COLUMN_NAMES_CLIENT_STATUS = {"Client1Status","Client2Status","Client3Status", "Client4Status","Client5Status"};
    //
    private static final String TABLE_NAME = "client_status";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_NAME = "client_name";
    private static final String CLIENT_STATUS = "status";


    private static final String SMS_TABLE_CREATE =
            "CREATE TABLE " + SMS_TABLE_NAME + " (" +
                    COLUMN_NAMES[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAMES[1] + " TEXT, " +
                    COLUMN_NAMES[2] + " TEXT, " +
                    COLUMN_NAMES[3] + " TEXT, " +
                    COLUMN_NAMES[4] + " TEXT, " +
                    COLUMN_NAMES[5] + " TEXT, " +
                    COLUMN_NAMES[6] + " TEXT, " +
                    COLUMN_NAMES[7] + " TEXT);";

    private static final String TEST_TABLE_CREATE =
            "CREATE TABLE " + TEST_TABLE_NAME + " (" +
                    COLUMN_NAMES_TEST[0] + " TEXT, " +
                    COLUMN_NAMES_TEST[1] + " TEXT, " +
                    COLUMN_NAMES_TEST[2] + " TEXT, " +
                    COLUMN_NAMES_TEST[3] + " TEXT, " +
                    COLUMN_NAMES_TEST[4] + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

    /*private static final String CLIENT_STATUS_TABLE_CREATE =
            "CREATE TABLE " + CLIENT_STATUS_TABLE_NAME + " (" +
                    COLUMN_NAMES_CLIENT_STATUS[0] + " TEXT, " +
                    COLUMN_NAMES_CLIENT_STATUS[1] + " TEXT, " +
                    COLUMN_NAMES_CLIENT_STATUS[2] + " TEXT, " +
                    COLUMN_NAMES_CLIENT_STATUS[3] + " TEXT, " +
                    COLUMN_NAMES_CLIENT_STATUS[4] + " TEXT);";*/

   /* private static final String SMSQueue_TABLE_CREATE =
            "CREATE TABLE " + SMSQueue_TABLE_NAME + " (" +
                    COLUMN_NAMES_SMSQueue[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAMES_SMSQueue[1] + " TEXT);";*/

    private static final String APPOBSERVER_TABLE_CREATE =
            "CREATE TABLE " + APPOBSERVER_TABLE_NAME + " (" +
                    COLUMN_NAMES_APPOBSERVER[0] + " TEXT, " +
                    COLUMN_NAMES_APPOBSERVER[1] + " TEXT, " +
                    COLUMN_NAMES_APPOBSERVER[2] + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";


    public SmsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creates the database if it doesn't exist and adds Messages table
        // Execute SQL query.
        //db.execSQL(SMSQueue_TABLE_CREATE);
        db.execSQL(SMS_TABLE_CREATE);
        db.execSQL(TEST_TABLE_CREATE);
        db.execSQL(APPOBSERVER_TABLE_CREATE);
        //db.execSQL(CLIENT_STATUS_TABLE_CREATE);

        try {
            String CREATE_USERINFO = "CREATE TABLE " + TABLE_NAME + "("
                    + CLIENT_ID + "  INTEGER PRIMARY KEY, "
                    + CLIENT_NAME + " TEXT, "
                    + CLIENT_STATUS + " TEXT "
                    +")";

            db.execSQL(CREATE_USERINFO);
        } catch (Exception e) {

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int addSms(Sms sms) {
        // Put Sms data into ContentValues object to insert into database
        ContentValues row = new ContentValues();
        row.put(this.COLUMN_NAMES[1], sms.campaignName);
        row.put(this.COLUMN_NAMES[2], sms.recipientsNumber);
        row.put(this.COLUMN_NAMES[3], sms.message);
        row.put(this.COLUMN_NAMES[4], sms.messageDate);
        row.put(this.COLUMN_NAMES[5], sms.messageTime);
        row.put(this.COLUMN_NAMES[6], sms.messageStatus);
        row.put(this.COLUMN_NAMES[7], sms.apiType);

        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // insert sms details into a new row, return the id of the new row.
        long longid = db.insert(SMS_TABLE_NAME, null, row);
        db.close();

        // convert id from long to int and return
        int SmsID = (int) longid;
        return SmsID;
    }

    /*public void addSmsIntoQueue(String campaignScheduleDateTime) {
        // Put Sms data into ContentValues object to insert into database
        ContentValues row = new ContentValues();
        row.put(this.COLUMN_NAMES_SMSQueue[1], campaignScheduleDateTime);

        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // insert sms details into a new row, return the id of the new row.
        db.insert(SMSQueue_TABLE_NAME, null, row);
        db.close();
    }*/

    public int addTestInfo(Test test) {
        // Put Sms data into ContentValues object to insert into database
        ContentValues row = new ContentValues();
        row.put(this.COLUMN_NAMES_TEST[0], test.SmsID);
        row.put(this.COLUMN_NAMES_TEST[1], test.recipientsNumber);
        row.put(this.COLUMN_NAMES_TEST[2], test.message);
        row.put(this.COLUMN_NAMES_TEST[3], test.messageDateTime);

        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // insert sms details into a new row, return the id of the new row.
        long longid = db.insert(TEST_TABLE_NAME, null, row);
        db.close();

        // convert id from long to int and return
        int SlNO = (int) longid;
        return SlNO;
    }

    public void addAppObserverInfo(String action,String calenderDateTime) {
        // Put Sms data into ContentValues object to insert into database
        ContentValues row = new ContentValues();
        row.put(this.COLUMN_NAMES_APPOBSERVER[0], action);
        row.put(this.COLUMN_NAMES_APPOBSERVER[1], calenderDateTime);

        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // insert sms details into a new row, return the id of the new row.
        db.insert(APPOBSERVER_TABLE_NAME, null, row);
        db.close();
    }

    public void removeSms(Integer SmsID) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // Create where clause
        String whereClause = "SmsID = '" + SmsID + "'";

        // Remove row from table with SmsID passed.
        db.delete(SMS_TABLE_NAME, whereClause, null);
    }

    public ArrayList<Sms> getSmsByID(int searchID) {
        // Get the readable database.
        SQLiteDatabase db = this.getReadableDatabase();

        // Get sms by ID
        Cursor result = db.rawQuery("SELECT * FROM Messages WHERE SmsID =" + searchID, null);

        // Create list of sms objects
        ArrayList<Sms> sms = new ArrayList<Sms>();

        // For number of sms retrieved create a sms object with name, number, message, message date, message time.
        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            sms.add(new Sms(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6),result.getString(7)));
        }

        return sms;
    }

    public void updateSmsToSent(Integer SmsID) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // Put Sms ID into ContentValues object
        ContentValues cv = new ContentValues();
        cv.put("messageStatus", "Sent");

        // Update messageStatus to Sent where SMSID is equal to SMS ID passed
        db.update(SMS_TABLE_NAME, cv, "SmsID=" + SmsID, null);
    }

    /*public void updateClientStatus(String clientName,String status) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // Put Sms ID into ContentValues object
        ContentValues cv = new ContentValues();
        cv.put(clientName, status);

        // Update messageStatus to Sent where SMSID is equal to SMS ID passed
        db.update(CLIENT_STATUS_TABLE_NAME, cv, null, null);
    }*/

    public void updateSmsToFailed(Integer SmsID) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // Put Sms ID into ContentValues object update SMS
        ContentValues cv = new ContentValues();
        cv.put("messageStatus", "Failed");

        // Update messageStatus to Failed where SMSID is equal to SMS ID passed
        db.update(SMS_TABLE_NAME, cv, "SmsID=" + SmsID, null);
    }

    public int retrieveSmsID(Sms sms) {
        // Get the readable database.
        SQLiteDatabase db = this.getReadableDatabase();

        // Create where clause from details of SMS object
        String whereClause = "Name = '" + sms.campaignName + "' AND Number = '" + sms.recipientsNumber + "' AND messageDate= '" + sms.messageDate + "' AND messageTime= '" + sms.messageTime + "' AND message = '" + sms.message + "'";

        // Returns the number of affected rows. 0 means no rows were deleted.
        Cursor result = db.rawQuery("SELECT SmsID FROM Messages WHERE " + whereClause, null);
        int retrievedID = 0;

        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            retrievedID = result.getInt(0);
        }
        return retrievedID;
    }

    public ArrayList<Sms> getPendingSmsList() {
        // Get readable database
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database for all sms where messageStatus is pending
        Cursor result = db.rawQuery("SELECT * FROM ClientStatus WHERE messageStatus ='Pending' ORDER BY SmsID DESC", null);

        // Create list of sms objects
        ArrayList<Sms> sms = new ArrayList<Sms>();

        // For number of sms retrieved create a sms object with name, number, message, message date, message time.
        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            sms.add(new Sms(result.getString(1), result.getString(2), result.getString(3), result.getString(4),
                    result.getString(5), result.getString(6),result.getString(7)));
        }
        return sms;
    }

    /*public String getClientStatus(String clientName){
        // Get readable database
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database for all sms where messageStatus is pending
        Cursor result = db.rawQuery("SELECT * FROM ClientStatus WHERE ClientName = "+clientName, null);
        return result.getString(0);
    }*/


    // insert data
    public void insertClienInfo(String name, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLIENT_NAME, name);
        values.put(CLIENT_STATUS, status);

        db.insert(TABLE_NAME, null, values);
        Log.d("TAG", "Insert success");
        db.close(); // Closing database connection
    }

    public String getClientStatus(String name) {
        String status = null;
        try {
            String selectQuery = "SELECT status FROM "+ TABLE_NAME +" WHERE client_name= '"+ name +"'" ;

            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);

            cursor.moveToFirst();

            status = cursor.getString(0) ;
//            AppController.getAppController().getInAppNotifier().log("token", token);

        }

        catch (Exception ex) {
            Log.e("TAG", ex.toString());
        }

        return status;
    }

    public int getClientExtistence() {
        String status = null;
        try {
            String selectQuery = "SELECT COUNT(*) FROM "+ TABLE_NAME +" " ;

            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);

            cursor.moveToFirst();

            status = cursor.getString(0) ;
//            AppController.getAppController().getInAppNotifier().log("token", token);

        }

        catch (Exception ex) {
            Log.e("TAG", ex.toString());
        }

        return Integer.valueOf(status);
    }

    public boolean updateClientStatus( String name, String status ) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        long l = db1.update(TABLE_NAME, contentValues, "client_name=?", new String[]{name});

        if (l != -1) {
            return true;
        } else {
            return false;
        }
    }




}



