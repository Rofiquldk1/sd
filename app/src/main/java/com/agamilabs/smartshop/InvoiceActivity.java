package com.agamilabs.smartshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.agamilabs.smartshop.adapter.RvAdapter_invoiceItemList;
import com.agamilabs.smartshop.database.DbHelper;
import com.agamilabs.smartshop.model.InvoiceItem;
import com.agamilabs.smartshop.model.InvoiceModel;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InvoiceActivity extends AppCompatActivity implements View.OnClickListener {

    private DbHelper dbHelper;
    private DatePicker datePicker;
    private Calendar calendar;
    private int day, month, year;
    private double discount, deduction,total = 0;
    private double subTotal = 0;


    private TextView textView_customerName, textView_currentDate, textView_dueDate, textView_subTotal, textView_total;
    private RecyclerView rv;
    private Button btn_invoiceCancel, btn_invoiceSave;
    private EditText editText_discount, editText_deduction;
    private Dialog dialog;

    private RvAdapter_invoiceItemList adapterInvoiceItemList;
    private List<InvoiceItem> invoiceItemList;
    private ArrayList<String> customerArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        customerArrayList = new ArrayList<>();
        customerArrayList.add("Rofiq");
        customerArrayList.add("Saruj");
        customerArrayList.add("Forhad");
        customerArrayList.add("Sifat");
        customerArrayList.add("Rakib");
        customerArrayList.add("Customer 6");
        customerArrayList.add("Customer 7");
        customerArrayList.add("Customer 8");
        customerArrayList.add("Customer 9");
        customerArrayList.add("Customer 10");


//        showDate(day, month, year);

        rv = findViewById(R.id.rv_invoiceItemList);
        btn_invoiceCancel = findViewById(R.id.btn_invoiceCancel);
        btn_invoiceSave = findViewById(R.id.btn_invoiceSave);
        textView_customerName = findViewById(R.id.tv_customerName);
        textView_currentDate = findViewById(R.id.tv_currentDate);
        textView_dueDate = findViewById(R.id.tv_dueDate);
        textView_subTotal = findViewById(R.id.tv_subTotal);
        textView_total = findViewById(R.id.tv_Total);
        editText_discount = findViewById(R.id.edtTxt_discount);
        editText_deduction = findViewById(R.id.edtTxt_deduction);

        textView_customerName.setOnClickListener(this);
        textView_currentDate.setOnClickListener(this);
        textView_dueDate.setOnClickListener(this);
        btn_invoiceCancel.setOnClickListener(this);
        btn_invoiceSave.setOnClickListener(this);

        dateHandler();

//        intentHandler();
        rvHandler();
        totalBillHandler();
    }

        private void totalBillHandler() {
            for (int i = 0; i<invoiceItemList.size(); i++){
                InvoiceItem current = invoiceItemList.get(i);
                subTotal = subTotal + current.getItem_bill();
    //            String num = String.valueOf((int)current.getItem_bill());
            }
            textView_subTotal.setText(String.valueOf((int)subTotal));
            textView_total.setText(String.valueOf((int)subTotal));

            editText_discount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (TextUtils.isEmpty(s.toString().trim())) {
                        discount = 0;
                        textView_total.setText(String.valueOf((int) (subTotal - deduction)));
                    } else
                    {
                        discount = Double.parseDouble(s.toString());
                        double temp = subTotal - (discount + deduction);
                        total = temp;
                        textView_total.setText(String.valueOf((int) temp));
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            editText_deduction.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (TextUtils.isEmpty(s.toString().trim()))
                    {
                        deduction = 0;
                        textView_total.setText(String.valueOf((int) (subTotal - discount)));
                    } else {
                        deduction = Double.parseDouble(s.toString());
                        double temp = subTotal - (deduction + discount);
                        total = temp;
                        textView_total.setText(String.valueOf((int) temp));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


        }

    private void dateHandler() {
        calendar = Calendar.getInstance();

        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        showCurrentDate(day, month+1, year);
        showDueDate(day, month+1, year);
    }

    private void showDueDate(int day, int month, int year) {
        String dayString = String.valueOf(day);
        String dayMonth = String.valueOf(month);
        String dayYear = String.valueOf(year);
        textView_dueDate.setText(dayString+"/"+dayMonth+"/"+dayYear);
    }

    private void showCurrentDate(int day, int month, int year) {
        String dayString = String.valueOf(day);
        String dayMonth = String.valueOf(month);
        String dayYear = String.valueOf(year);
        textView_currentDate.setText(dayString+"/"+dayMonth+"/"+dayYear);


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        } else if (id == 1000) {
            return new DatePickerDialog(this, myDateListener2, year, month, day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showCurrentDate(arg3, arg2+1, arg1);
                }
            };
    private DatePickerDialog.OnDateSetListener myDateListener2 = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDueDate(arg3, arg2+1, arg1);
                }
            };


    private void rvHandler() {
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");

        invoiceItemList = (ArrayList<InvoiceItem>) args.getSerializable("selectedProductList");
        adapterInvoiceItemList = new RvAdapter_invoiceItemList(this, invoiceItemList);
        rv.setAdapter(adapterInvoiceItemList);
        adapterInvoiceItemList.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_invoiceCancel:
//                dbHelper.mDeleteProductList();
/*                Intent intent = new Intent(this, FullScannerActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();*/
                Toast.makeText(this, "Cancel Clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.tv_customerName:
                dialog = new Dialog(InvoiceActivity.this);
                dialog.setContentView(R.layout.dialog_searchable_spinner_layout);
                dialog.getWindow().setLayout(500, 800);
//                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText editText = dialog.findViewById(R.id.edtTxt_searchableText);
                ListView listView = dialog.findViewById(R.id.lv_searchableCustomerView);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(InvoiceActivity.this, R.layout.lv_customer_search_list, customerArrayList);

                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //filter arrayList
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        textView_customerName.setText(adapter.getItem(position));
                        dialog.dismiss();
                    }
                });
                break;

            case R.id.btn_invoiceSave:
                dialog = new Dialog(InvoiceActivity.this);
                dialog.setContentView(R.layout.dialog_invoice_next_btn_layout);
                dialog.getWindow().setLayout(550, 350);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                dialog.setCancelable(false);

                TextView textView_yes = dialog.findViewById(R.id.tv_yes_InvoiceNextBtnDialog);
                TextView textView_no = dialog.findViewById(R.id.tv_no_InvoiceNextBtnDialog);

                textView_yes.setOnClickListener(this);
                textView_no.setOnClickListener(this);
                break;

            case R.id.tv_currentDate:
                showDialog(999);
                break;

            case R.id.tv_dueDate:
                showDialog(1000);
                break;

            case R.id.tv_yes_InvoiceNextBtnDialog:
                sendNetworkReq();
                break;

            case R.id.tv_no_InvoiceNextBtnDialog:
                dialog.dismiss();
                break;

        }
    }

    private void sendNetworkReq() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://www.google.com";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Intent rfActivity = new Intent(InvoiceActivity.this, RofiqActivity.class);
//                rfActivity.putExtra("invoiceResponse", response);
//                rfActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(rfActivity);
//                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InvoiceActivity.this, "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem;

        menuItem = menu.add(Menu.NONE, R.id.menu_invoiceView, 0, "View invoice" );
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_invoiceView:
                Intent invoiceViewActivity = new Intent(InvoiceActivity.this, InvoiceViewerActivity.class);

                startActivity(invoiceViewActivity);
        }

        return super.onOptionsItemSelected(item);
    }
}