package com.agamilabs.smartshop.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.controller.AppController;
import com.agamilabs.smartshop.database.SmsDatabaseHelper;
import com.agamilabs.smartshop.model.Sms;
import com.agamilabs.smartshop.services.AlarmReceiver;
import com.agamilabs.smartshop.services.NotificationReceiver;
import com.agamilabs.smartshop.services.NotifyUser;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.READ_CONTACTS;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class NewCampaignActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<String>, Response.ErrorListener {
    private ExpandableRelativeLayout expandableLayout;
    private Button btn_expand,btn_save,btn_contacts;
    private ImageButton imageBtn_DP,imageBtn_TP;
    private TextView txtDate,txtTime,txt_count_valid_num,txtNotificationStatus;
    private EditText editText_recipients,edittext_campaignName,edittext_message, editText_extra_recipient;
    private Switch notificationsSwitch;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private ArrayList<String> phoneNumList = new ArrayList<String>();
    private String url = "/connector/index5.php";
    private String notifyTimeBeforeScheduling;
    private String isChecked="false",format,selectTime,selectDate,selectTimeForNotification,selectDateForNotification;
    RequestQueue requestQueue,requestQueueDuplicate;
    public static NewCampaignActivity INSTANCE;
    public static final int MULTIPLE_PERMISSION_CODE = 101;
    public static final int SMS_PERMISSION_CODE = 102;
    public static final int CONTACTS_PERMISSION_CODE = 103;
    private boolean smsSentPermission = false;
    private SmsDatabaseHelper smsDatabaseHelper;
    public int setHour = -1, setMinute = -1, setDay = -1, setMonth = -1, setYear = -1;
    private Spinner spinner_notifications_time;
    private CheckBox checkBoxAgree;
    String[] appPermissions = {
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CONTACTS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_campaign);

        INSTANCE = this;
        init();

        //Initialise database
        smsDatabaseHelper = new SmsDatabaseHelper(this);
        requestQueue = Volley.newRequestQueue(this);
        requestQueueDuplicate = Volley.newRequestQueue(this);

        btn_expand.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_contacts.setOnClickListener(this);
        imageBtn_DP.setOnClickListener(this);
        imageBtn_TP.setOnClickListener(this);
        checkBoxAgree.setOnClickListener(this);

        editText_recipients.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editText_recipients.getText().toString();
                String[] array = str.split(",");
                int count = 0;

                for(int i=0;i<array.length;i++){
                    if(array[i].trim().length()==11){
                        String newStr = array[i];
                        boolean check=true;
                        for(int j=0;j<newStr.length();j++){
                            char ch = newStr.charAt(j);
                            //Log.d("value",Integer.valueOf(ch).toString());
                            if(Integer.valueOf(ch)<48 || Integer.valueOf(ch)>57){
                                check=false;
                                break;
                            }
                        }
                        if(check){
                            count++;
                            txt_count_valid_num.setText(String.valueOf(count));
                            txt_count_valid_num.setTextColor(Color.BLACK);
                        }
                        else {
                            txt_count_valid_num.setText("Please enter valid phone number");
                            txt_count_valid_num.setTextColor(Color.RED);
                        }
                    }
                    else if(str.length()>0 && str.charAt(str.length()-1) == ','){
                        txt_count_valid_num.setText("Please enter valid phone number");
                        txt_count_valid_num.setTextColor(Color.RED);
                    }
                    else{
                        txt_count_valid_num.setText(String.valueOf(count));
                        txt_count_valid_num.setTextColor(Color.BLACK);
                    }
                }
            }
        });

        //smsDatabaseHelper.addSmsIntoQueue("12/3/2021 ");
        //Log.d("174",smsDatabaseHelper.getQueueSize("2/23/2021")+"");


        SharedPreferences NotificationsPref = getSharedPreferences("switchStaus", 0);
        boolean notificationsStatusOn = NotificationsPref.getBoolean("notificationSwitch", true);

        // Set switch to value in shared preferences
        notificationsSwitch.setChecked(notificationsStatusOn);
        notificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // if notifications is checked display notifications on, if not display off
                if (notificationsSwitch.isChecked()){
                    Toast.makeText(getApplicationContext(), "Campaign Notifications turned on", Toast.LENGTH_SHORT).show();
                    txtNotificationStatus.setText("On");
                }else{
                    Toast.makeText(getApplicationContext(), "Campaign Notifications turned off", Toast.LENGTH_SHORT).show();
                    txtNotificationStatus.setText("Off");
                }
                // Update shared preferences to switch checked status
                SharedPreferences NotificationsPref = getSharedPreferences("switchStaus", 0);
                SharedPreferences.Editor editor = NotificationsPref.edit();
                editor.putBoolean("notificationSwitch", isChecked);
                editor.commit();
            }
        });

        ArrayAdapter<String> spinnerEarlyNotifyTimeArrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.early_notifications_time));
        spinner_notifications_time.setAdapter(spinnerEarlyNotifyTimeArrayAdapter);

        checkAndRequestPermissions();
        loadDateTime();
    }

    private void loadDateTime() {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());               // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour

        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        String monthName = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()).substring(0,3);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

        setYear=mYear;
        setMonth=mMonth;
        setDay=mDay;
        setHour=mHour;
        setMinute=mMinute;

        if (mHour == 0) {
            mHour += 12;
            format = "AM";
        } else if (mHour == 12) {
            format = "PM";
        } else if (mHour > 12) {
            mHour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        txtDate.setText(mDay + " " + (monthName) + ", " + mYear);
        txtTime.setText(mHour + ":" + mMinute+" "+format);
    }

    public void requestForSMSPermission() {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.SEND_SMS)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("For sending sms please give permission")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(NewCampaignActivity.this,
                                    new String[] {Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        }
    }

    private void requestContactPermission() {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_CONTACTS)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("For select number from contacts please give permission")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(NewCampaignActivity.this,
                                    new String[] {READ_CONTACTS},CONTACTS_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[] {READ_CONTACTS}, CONTACTS_PERMISSION_CODE);
        }
    }

    private void checkAndRequestPermissions() {
        List<String> listPermissionsNeeded = new ArrayList<>();

        for(String perm : appPermissions){
            //check all permissions one by one
            if(ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED){
                listPermissionsNeeded.add(perm);
            }
        }

        if(!listPermissionsNeeded.isEmpty()){
            //request all permissions
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSION_CODE);
        }
    }

    private void selectContact() {
        Intent intent = new Intent(NewCampaignActivity.this, SelectContactActivity.class);
        startActivity(intent);
    }

    public static NewCampaignActivity getActivityInstance()
    {
        return INSTANCE;
    }

    public void sendDuplicateInfo(String schedule_no, String message_title, String message, String catid, String session, String apiid, String schedule_starting_datetime, String schedule_particular_number, String priority, String status, String schedule_entry_datetime,
                                  String total_subscribers, String total_sent_subscribers, String total_cost){
        final HashMap<String, String> params = new HashMap<>();

        params.put("campaign_name",message_title);
        params.put("recipients", "");
        params.put("message", message);
        params.put("campaign_start_date_time", schedule_starting_datetime);
        params.put("api_id", apiid);
        params.put("exttra_recipient", "");
        params.put("saveAsTemplate","");

        //Log.d("Total",params.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                NewCampaignActivity.this, NewCampaignActivity.this) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        requestQueueDuplicate.add(stringRequest);
    }

    private void init() {
        expandableLayout = findViewById(R.id.expandableLayout);
        btn_expand = findViewById(R.id.expandableButton);
        btn_save = findViewById(R.id.btn_save);
        btn_contacts = findViewById(R.id.btn_contacts);
        imageBtn_DP = findViewById(R.id.imagebtn_datePicker);
        imageBtn_TP = findViewById(R.id.imagebtn_TimePicker);
        txtDate = findViewById(R.id.text_date);
        txtTime = findViewById(R.id.text_time);
        txt_count_valid_num = findViewById(R.id.text_count_valid_num);
        txtNotificationStatus = findViewById(R.id.txt_nofication_status);
        edittext_campaignName = findViewById(R.id.edittext_campaignName);
        editText_recipients = findViewById(R.id.edittext_recipients);
        edittext_message = findViewById(R.id.edittext_message);
        editText_extra_recipient = findViewById(R.id.edittext_extra_recipients);
        radioGroup = findViewById(R.id.radioGroup);
        notificationsSwitch = findViewById(R.id.notificationsSwitch);
        spinner_notifications_time = findViewById(R.id.spinner_early_notify_time);
        checkBoxAgree = findViewById(R.id.ck_box_agree);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View view) {
        if(view == btn_expand){
            expandableLayout.toggle();
        }
        else if(view == imageBtn_DP){
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            String monthName = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()).substring(0,3);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            setYear=year;
                            setMonth=monthOfYear;
                            setDay=dayOfMonth;
                            selectDate = dayOfMonth + "/" + (monthOfYear+1) + "/" + year;
                            selectDateForNotification = dayOfMonth + " " + (monthName) + ", " + year;
                            txtDate.setText(selectDateForNotification);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        else if(view == imageBtn_TP){
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            selectTime = hourOfDay + ":" + minute + ":00";
                            setHour=hourOfDay;
                            setMinute=minute;

                            if (hourOfDay == 0) {
                                hourOfDay += 12;
                                format = "AM";
                            } else if (hourOfDay == 12) {
                                format = "PM";
                            } else if (hourOfDay > 12) {
                                hourOfDay -= 12;
                                format = "PM";
                            } else {
                                format = "AM";
                            }

                            selectTimeForNotification = hourOfDay + ":" + minute+" "+format;
                            txtTime.setText(selectTimeForNotification);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        else if(view == btn_save && checkBoxAgree.isChecked()){
            validateInput();
        }
        else if (view == btn_contacts){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(NewCampaignActivity.this,
                        READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    //selectContact();
                } else {
                    //requestContactPermission();
                }
            }
        }
        else if (view == checkBoxAgree){
            if (((CheckBox) view).isChecked()) {
                btn_save.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else {
                btn_save.setBackgroundColor(getResources().getColor(R.color.darkGray));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void validateInput() {
        // Assign input to variables
        final String campaignName = edittext_campaignName.getText().toString();
        final String recipients = editText_recipients.getText().toString();
        final String messageText = edittext_message.getText().toString();

        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);

        if (campaignName.isEmpty()) // Ensure name is not empty
        {
            Toast.makeText(getApplicationContext(), "Please enter a campaign name.", Toast.LENGTH_SHORT).show();
        } else if (recipients.isEmpty()) // Ensure phone number is not empty.
        {
            Toast.makeText(getApplicationContext(), "Please enter recipients phone number.", Toast.LENGTH_SHORT).show();
        } else if (messageText.isEmpty()) // Ensure message is not empty.
        {
            Toast.makeText(getApplicationContext(), "Please enter a message.", Toast.LENGTH_SHORT).show();
        } else if (setDay == -1 || setMonth == -1 || setYear == -1)  // Ensure date has been selected
        {
            Toast.makeText(getApplicationContext(), "Please select a date", Toast.LENGTH_SHORT).show();
        } else if (setHour == -1 || setMinute == -1) // Ensure a time has been selected.
        {
            Toast.makeText(getApplicationContext(), "Please select a time", Toast.LENGTH_SHORT).show();
        } else if (validateSelectedDateTime() == FALSE) // Compare dates, compare to returns negative number if selected date is less than current date
        {
            Toast.makeText(getApplicationContext(), "SMS must be scheduled for a future time", Toast.LENGTH_SHORT).show();

        } else if (AirplaneModeOn(getApplicationContext()) == TRUE) // Check if airplane mode is on
        {
            // Options for dialog
            String[] options = {"Continue to schedule", "Do not schedule", "Cancel"};

            // Build dialog, set title and items as options
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("SMS will not send in airplane mode. Please select an option:");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int selectedOption) {
                    // listen for selected item, check selected item and perform appropriate action
                    if (selectedOption == 0) {
                        if (validateSelectedDateTime() == FALSE)  // Ensure time has not changed into past
                        {
                            Toast.makeText(getApplicationContext(), "Time has changed, SMS must be scheduled for a future time", Toast.LENGTH_SHORT).show();
                        } else {
                            addToSms(campaignName, recipients, messageText,radioButton.getText().toString());
                        }
                    } else if (selectedOption == 1) {
                        Toast.makeText(NewCampaignActivity.this, "SMS has not been scheduled", Toast.LENGTH_LONG).show();
                        resetInput();
                    } else if (selectedOption == 2) {
                        //Do nothing as user has canceled

                    } else {
                        Toast.makeText(NewCampaignActivity.this, "Sorry an error occurred.", Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.show();
        } else //Schedule SMS
        {
            loadArrayList();
            notifyTimeBeforeScheduling = spinner_notifications_time.getSelectedItem().toString();
            SharedPreferences NotificationsPref = getApplicationContext().getSharedPreferences("switchStaus", 0);
            boolean notifcationsOn = NotificationsPref.getBoolean("notificationSwitch", true);

            final HashMap<String, String> params = new HashMap<>();
            params.put("campaign_name",campaignName);
            params.put("recipients", phoneNumList.toString());
            params.put("message", messageText);
            params.put("campaign_start_date_time", selectDate+" "+selectTime);
            params.put("isNotificatioOn",notifcationsOn+"");
            params.put("notifyTimeBeforeMesaging",notifyTimeBeforeScheduling);
            params.put("api_type", radioButton.getText().toString());
            params.put("exttra_recipient", editText_extra_recipient.getText().toString());
            params.put("saveAsTemplate",isChecked);

            //Log.d("1",notifyTimeBeforeScheduling.trim().substring(0,2).trim());

            /*AppController.getAppController().getAppNetworkController().makeRequest(
                    url, NewCampaignActivity.this, NewCampaignActivity.this, params);*/
            /*StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    NewCampaignActivity.this, NewCampaignActivity.this) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }
            };
            requestQueue.add(stringRequest);*/

            if(radioButton.getText().toString().equalsIgnoreCase("Device API")){
                if (ContextCompat.checkSelfPermission(NewCampaignActivity.this,
                        Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    addToSms(campaignName,phoneNumList.toString(),messageText,radioButton.getText().toString());
                } else {
                    requestForSMSPermission();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static boolean AirplaneModeOn(Context context) {
        // Return true if airplane more is on
        return Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }

    // Validate the selected date and time is in the future. Return True if date is in future
    private boolean validateSelectedDateTime() {
        Boolean validDateTime;
        // Get current date and time
        Date currentDateTime = Calendar.getInstance().getTime();
        // Get converted date and time
        Date selectedDateTime = convertSelectedDateTime();

        if (selectedDateTime.compareTo(currentDateTime) < 0) // Compare dates, returns negative number if selected date is less than current date
        {
            validDateTime = FALSE;
        } else {
            validDateTime = TRUE;
        }
        return validDateTime;
    }

    private boolean validateSelectedDateTimeForEarlySchedule() {
        Boolean validDateTimeForEarlySchedule;

        Date currentDateTime = Calendar.getInstance().getTime();
        // Get converted date and time
        Date selectedDateTime = convertSelectedDateTime();

        long diff = selectedDateTime.getTime() - currentDateTime.getTime();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);

        if (minutes > Integer.valueOf(notifyTimeBeforeScheduling.trim().substring(0,2).trim()))
        {
            validDateTimeForEarlySchedule = TRUE;
        } else {
            validDateTimeForEarlySchedule = FALSE;
        }
        return validDateTimeForEarlySchedule;
    }

    // Convert selected date and time to date format
    private Date convertSelectedDateTime() {
        Date convertedDateTime = null;
        String selectedDateTime = "";

        // Add 1 to month as months are between 0-11
        int selectedMonth = (setMonth + 1);

        // Join integers and convert to String
        selectedDateTime += setYear + "" + "" + String.format("%02d", selectedMonth) + "" + String.format("%02d", setDay) + "" + String.format("%02d", setHour) + "" + String.format("%02d", setMinute);

        try {
            //Convert String to date format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            convertedDateTime = sdf.parse(selectedDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDateTime;
    }

    public void addToSms(String campaignName, String phoneNumbers, String messageText,String api_type) {
        String name = campaignName;
        String numbers = phoneNumbers;
        String message = messageText;
        String messageDate = Integer.toString(setDay) + "/" + Integer.toString(setMonth) + "/" + Integer.toString(setYear); //Convert integers to string
        String messageTime = String.format("%02d:%02d", setHour, setMinute);
        String messageStatus = "Pending";
        String apiType = api_type;

        // Start multi-thread to insert sms to database and start alarm manager
        ScheduleSmsAsyncTask task = new ScheduleSmsAsyncTask();
        task.execute(name, numbers,message, messageDate, messageTime, messageStatus,apiType);
    }

    public void generateNewSchedule(String campaignName, String recipients, String campaignMessage, String campaignStartDateTime,
                                 String notificationOn, String notifyTimeBeforeMesaging, String apiType){
        String messageStatus = "Pending";
        // Update shared preferences to switch
        SharedPreferences NotificationsPref = getSharedPreferences("switchStaus", 0);
        SharedPreferences.Editor editor = NotificationsPref.edit();
        editor.putBoolean("notificationSwitch", Boolean.valueOf(notificationOn));
        editor.commit();
        //update time for scheduling
        notifyTimeBeforeScheduling = notifyTimeBeforeMesaging;

        setDay = Integer.valueOf(campaignStartDateTime.substring(0,2));
        setMonth = Integer.valueOf(campaignStartDateTime.substring(3,5))-1;
        setYear = Integer.valueOf(campaignStartDateTime.substring(6,10));
        setHour = Integer.valueOf(campaignStartDateTime.substring(11,13));
        setMinute = Integer.valueOf(campaignStartDateTime.substring(14,16));

        String messageDate = Integer.toString(setDay) + "/" + Integer.toString(setMonth) + "/" + Integer.toString(setYear); //Convert integers to string
        String messageTime = String.format("%02d:%02d", setHour, setMinute);

        ScheduleSmsAsyncTask task = new ScheduleSmsAsyncTask();
        task.execute(campaignName,recipients,campaignMessage, messageDate, messageTime, messageStatus,apiType);
    }

    private class ScheduleSmsAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... string) {
            String result = "SMS Successfully Scheduled";
            try {
                // Construct a Sms object and pass it to the helper for database insertion
                int SmsID = smsDatabaseHelper.addSms(new Sms(string[0], string[1], string[2], string[3], string[4], string[5], string[6]));

                // Create calendar with selected date and time
                Calendar calenderForSendingMsg = Calendar.getInstance();
                calenderForSendingMsg.set(Calendar.YEAR, setYear);
                calenderForSendingMsg.set(Calendar.MONTH, setMonth);
                calenderForSendingMsg.set(Calendar.DAY_OF_MONTH, setDay);
                calenderForSendingMsg.set(Calendar.HOUR_OF_DAY, setHour);
                calenderForSendingMsg.set(Calendar.MINUTE, setMinute);
                calenderForSendingMsg.set(Calendar.SECOND, 0);

                Calendar calendarForNotifyUser = Calendar.getInstance();
                if(notifyTimeBeforeScheduling.trim().equals("60 min")){
                    calendarForNotifyUser.set(Calendar.YEAR, setYear);
                    calendarForNotifyUser.set(Calendar.MONTH, setMonth);
                    calendarForNotifyUser.set(Calendar.DAY_OF_MONTH, setDay);
                    calendarForNotifyUser.set(Calendar.HOUR_OF_DAY, setHour-1);
                    calendarForNotifyUser.set(Calendar.MINUTE, setMinute);
                    calendarForNotifyUser.set(Calendar.SECOND, 0);
                }
                else {
                    calendarForNotifyUser.set(Calendar.YEAR, setYear);
                    calendarForNotifyUser.set(Calendar.MONTH, setMonth);
                    calendarForNotifyUser.set(Calendar.DAY_OF_MONTH, setDay);
                    calendarForNotifyUser.set(Calendar.HOUR_OF_DAY, setHour);
                    calendarForNotifyUser.set(Calendar.MINUTE, setMinute-Integer.valueOf(notifyTimeBeforeScheduling.trim().substring(0,2).trim()));
                    calendarForNotifyUser.set(Calendar.SECOND, 0);
                }

                if(validateSelectedDateTimeForEarlySchedule()){
                    AlarmManager alarmManagerForEarlySchedule = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intentForEarlySchedule = new Intent(NewCampaignActivity.this, NotifyUser.class);
                    intentForEarlySchedule.putExtra("scheduleType", "earlySchedule");
                    intentForEarlySchedule.putExtra("name", edittext_campaignName.getText().toString());
                    intentForEarlySchedule.putExtra("NotificationMsg",selectTimeForNotification+" "+selectDateForNotification);
                    intentForEarlySchedule.putExtra("SmsID", SmsID);

                    PendingIntent pendingIntentForEarlySchedule = PendingIntent.getBroadcast(getApplicationContext(),SmsID, intentForEarlySchedule, 0);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManagerForEarlySchedule.setExact(AlarmManager.RTC_WAKEUP, calendarForNotifyUser.getTimeInMillis(),pendingIntentForEarlySchedule);
                    } else {
                        alarmManagerForEarlySchedule.set(AlarmManager.RTC_WAKEUP,calendarForNotifyUser.getTimeInMillis(), pendingIntentForEarlySchedule);
                    }
                }

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE); // Pass SmsID to AlarmReceiver class
                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.putExtra("SmsID", SmsID); //Set SmsID as unique id, Set time to calender, Start alarm
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), SmsID, intent, 0);

                int ALARM_TYPE = AlarmManager.RTC_WAKEUP;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(ALARM_TYPE,calenderForSendingMsg.getTimeInMillis(), pendingIntent);
                } else {
                    alarmManager.set(ALARM_TYPE,calenderForSendingMsg.getTimeInMillis(), pendingIntent);
                }

            } catch (Exception e) {
                e.printStackTrace();
                result = "SMS failed to schedule";
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Display result message
            smsDatabaseHelper.addAppObserverInfo(result+" for "+
                            edittext_campaignName.getText().toString(), Calendar.getInstance().getTime()+"");

            Toast.makeText(NewCampaignActivity.this, result, Toast.LENGTH_SHORT).show();

            // Clear Sms Fields
            resetInput();
        }
    }

    private void resetInput() {
        edittext_campaignName.setText(" ");
        editText_recipients.setText(" ");
        edittext_message.setText(" ");
        editText_extra_recipient.setText(" ");
        txtDate.setText("Select Date");
        txtTime.setText("Select Time");
        isChecked="false";
        phoneNumList.clear();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == SMS_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addToSms(edittext_campaignName.getText().toString(),phoneNumList.toString(),
                        edittext_message.getText().toString(),radioButton.getText().toString());
            }
        } else if (requestCode == CONTACTS_PERMISSION_CODE)  {
            selectContact();
        }
    }

    private void loadArrayList() {
        String str = editText_recipients.getText().toString();
        String[] array = str.split(",");

        for(int i=0;i<array.length;i++){
            if(array[i].trim().length()==11){
                String newStr = array[i];
                boolean check=true;
                for(int j=0;j<newStr.length();j++){
                    char ch = newStr.charAt(j);
                    if(Integer.valueOf(ch)<48 || Integer.valueOf(ch)>57){
                        check=false;
                        break;
                    }
                }
                if(check){
                    //if element does not exists in the ArrayList, add it
                    if(!phoneNumList.contains(newStr)){
                        phoneNumList.add(newStr);
                    }
                    //phoneNumList.add(newStr);
                }
            }
        }


        if(editText_extra_recipient.getText().toString().trim().length() >= 11){
            String extra_recipient = editText_extra_recipient.getText().toString().trim().substring(0,11);
            if(extra_recipient.substring(0,2).equalsIgnoreCase("01")){
                /*if(!phoneNumList.contains(extra_recipient)){
                    phoneNumList.add(extra_recipient);
                }*/
                phoneNumList.add(extra_recipient);
            }else {
                return;
            }
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject object = new JSONObject(response);
            if (object.has("error") && !object.getBoolean("error")) {
                resetInput();
                startActivity(new Intent(NewCampaignActivity.this,CampaignActivity.class));
            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(object.getString("message"));
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Dismiss",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();

            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

        }
    }

    public void itemClicked(View v) {
        //code to check if this checkbox is checked!
        CheckBox checkBox = (CheckBox)v;
        if(checkBox.isChecked()){
           isChecked="true";
        }
    }

}