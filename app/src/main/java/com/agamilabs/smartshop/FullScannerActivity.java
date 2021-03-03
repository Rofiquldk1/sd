package com.agamilabs.smartshop;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agamilabs.smartshop.Interfaces.IcallBackTest;
import com.agamilabs.smartshop.adapter.RvAdapter_selectedProductView;
import com.agamilabs.smartshop.constants.Array_JSON;
import com.agamilabs.smartshop.database.DbHelper;
import com.agamilabs.smartshop.Interfaces.ProductDetailsInterface;
import com.agamilabs.smartshop.model.InvoiceItem;
import com.agamilabs.smartshop.model.InvoiceModel;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class FullScannerActivity extends BaseScannerActivity implements MessageDialogFragment.MessageDialogListener,
        ZXingScannerView.ResultHandler, FormatSelectorDialogFragment.FormatSelectorDialogListener,
        CameraSelectorDialogFragment.CameraSelectorDialogListener, ProductDetailsInterface, View.OnClickListener, IcallBackTest {
    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private static final String CAMERA_ID = "CAMERA_ID";

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 7;
    private RecyclerView rv_selectedProduct;
    private RvAdapter_selectedProductView rvAdapter_selectedProductView;
    private Dialog dialog_cross;
    private BottomSheetBehavior sheetBehavior;
    private RelativeLayout bottomsSheetLayout;
    private EditText editText_discount, editText_deduction;
    private TextView textView_subTotal, textView_total;
    private ViewGroup contentFrame;
    private Button btn_nxtProductList;
    private ImageButton imageButtonFlashOn, imageButtonFlashOff, imageButtonFocusOn, imageButtonFocusOff, imageButtonPersonSearch, imageButtonProductSearch;
    private Dialog dialog, dialogAddCustomer, dialogProductSearch;
    private TextView textViewCustomerName;
    private RelativeLayout relativeLayoutBottomSheetComponents;
    private LinearLayout linearLayoutBottomSheetCustomerName;
    private TextView textViewCartBadge;

    private DbHelper dbHelper;
    private List<InvoiceModel> invoiceModelList;
    private List<InvoiceItem> invoiceItemList;
    private ArrayList<String> customerArrayList;
    private ArrayList<String> productArrayList;

    private ZXingScannerView mScannerView;
    private boolean mFlash;
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;
    private int mCameraId = -1;

    private double discount, deduction, total = 0;
    private double subTotal = 0;
    IcallBackTest icallBackTest;


    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        checkAndroidVersion();
        if (state != null) {
            mFlash = state.getBoolean(FLASH_STATE, false);
            mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);
            mSelectedIndices = state.getIntegerArrayList(SELECTED_FORMATS);
            mCameraId = state.getInt(CAMERA_ID, -1);
        } else {
            mFlash = false;
            mAutoFocus = true;
            mSelectedIndices = null;
            mCameraId = -1;
        }

        setContentView(R.layout.activity_simple_scanner);
//        setupToolbar();
//        Objects.requireNonNull(getSupportActionBar()).hide();

        contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        bottomsSheetLayout = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomsSheetLayout);
        sheetBehaviorHandler();

        invoiceItemList = new ArrayList<>();
        mScannerView = new ZXingScannerView(this);
        rv_selectedProduct = findViewById(R.id.rv_productView);
        textView_subTotal = findViewById(R.id.tv_subTotal);
        textView_total = findViewById(R.id.tv_Total);
        editText_discount = findViewById(R.id.edtTxt_discount);
        editText_deduction = findViewById(R.id.edtTxt_deduction);
        imageButtonFlashOn = findViewById(R.id.imgBtn_flash_on);
        imageButtonFlashOff = findViewById(R.id.imgBtn_flash_off);
        imageButtonFocusOn = findViewById(R.id.imgBtn_focus_on);
        imageButtonFocusOff = findViewById(R.id.imgBtn_focus_off);
        imageButtonPersonSearch = findViewById(R.id.imgBtn_personSearch);
        imageButtonProductSearch = findViewById(R.id.imgBtn_prodcutSearch);
        textViewCustomerName = findViewById(R.id.tv_bottomSheetCustomerName);
        relativeLayoutBottomSheetComponents = findViewById(R.id.l_bottomSheet_components);
        linearLayoutBottomSheetCustomerName = findViewById(R.id.l_bottomSheet_customerName);
        textViewCartBadge = findViewById(R.id.tv_cartBadge);

        userList();
        productList();
        recyclerViewHandler();
        setupFormats();
        contentFrame.addView(mScannerView);

        imageButtonFocusOn.setOnClickListener(this);
        imageButtonFocusOff.setOnClickListener(this);
        imageButtonFlashOn.setOnClickListener(this);
        imageButtonFlashOff.setOnClickListener(this);
        imageButtonPersonSearch.setOnClickListener(this);
        imageButtonProductSearch.setOnClickListener(this);
//        totalBillHandler();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void userList() {
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
    }
    private void productList() {
        productArrayList = new ArrayList<>();
        productArrayList.add("Product 1");
        productArrayList.add("Product 2");
        productArrayList.add("Product 3");
        productArrayList.add("Product 4");
        productArrayList.add("Product 5");
        productArrayList.add("Product 6");
        productArrayList.add("Product 7");
        productArrayList.add("Product 8");
        productArrayList.add("Product 9");
        productArrayList.add("Product 10");
        productArrayList.add("Product 11");
        productArrayList.add("Product 12");
        productArrayList.add("Product 13");
        productArrayList.add("Product 14");
        productArrayList.add("Product 15");
        productArrayList.add("Product 16");
    }

    public void totalBillHandler() {
        subTotal = 0;
        textViewCartBadge.setText(String.valueOf(invoiceItemList.size()));
        if (invoiceItemList.size() == 0 && relativeLayoutBottomSheetComponents.getVisibility() == View.VISIBLE) {
            relativeLayoutBottomSheetComponents.setVisibility(View.GONE);
        } else {
            for (int i = 0; i < invoiceItemList.size(); i++) {
                InvoiceItem current = invoiceItemList.get(i);
                subTotal = subTotal + current.getItem_bill();
                //            String num = String.valueOf((int)current.getItem_bill());
            }
            textView_subTotal.setText(String.valueOf((int) subTotal));
            textView_total.setText(String.valueOf((int) subTotal));

            editText_discount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (TextUtils.isEmpty(s.toString().trim())) {
                        discount = 0;
                        textView_total.setText(String.valueOf((int) (subTotal - deduction)));
                    } else {
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
                    if (TextUtils.isEmpty(s.toString().trim())) {
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



    }

    private void sheetBehaviorHandler() {
        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void recyclerViewHandler() {
        rv_selectedProduct.setHasFixedSize(true);
        rv_selectedProduct.setLayoutManager(new LinearLayoutManager(this));
    }

    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions();

        } else {
            // code for lollipop and pre-lollipop devices
        }

    }

    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int wtite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
        Toast.makeText(this, "OnResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        dbHelper.mDeleteProductList();
        invoiceItemList.clear();
        Toast.makeText(this, "onDestroy!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
//        dbHelper.mDeleteProductList();
        mScannerView.stopCamera();
        closeMessageDialog();
        closeFormatsDialog();
//        Toast.makeText(this, "onPause!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Toast.makeText(this, "onStop!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
        outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices);
        outState.putInt(CAMERA_ID, mCameraId);
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem;
        if (mFlash) {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_on);
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_off);
        }
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);
        if (mAutoFocus) {
            menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, R.string.auto_focus_on);
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, R.string.auto_focus_off);
        }
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);
        menuItem = menu.add(Menu.NONE, R.id.menu_formats, 0, R.string.formats);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);
        menuItem = menu.add(Menu.NONE, R.id.menu_camera_selector, 0, R.string.select_camera);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);
        return super.onCreateOptionsMenu(menu);
    }
*/

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_flash:
                mFlash = !mFlash;
                if (mFlash) {
                    item.setTitle(R.string.flash_on);
                } else {
                    item.setTitle(R.string.flash_off);
                }
                mScannerView.setFlash(mFlash);
                return true;
            case R.id.menu_auto_focus:
                mAutoFocus = !mAutoFocus;
                if (mAutoFocus) {
                    item.setTitle(R.string.auto_focus_on);
                } else {
                    item.setTitle(R.string.auto_focus_off);
                }
                mScannerView.setAutoFocus(mAutoFocus);
                return true;
            case R.id.menu_formats:
                DialogFragment fragment = FormatSelectorDialogFragment.newInstance(this, mSelectedIndices);
                fragment.show(getSupportFragmentManager(), "format_selector");
                return true;
            case R.id.menu_camera_selector:
                mScannerView.stopCamera();
                DialogFragment cFragment = CameraSelectorDialogFragment.newInstance(this, mCameraId);
                cFragment.show(getSupportFragmentManager(), "camera_selector");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
*/


    @Override
    public void handleResult(Result rawResult) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
        }

        mJSON_parser(rawResult);
//        mSendVolleyRequest();
    }

    private void mSendVolleyRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.google.com";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
//                        textView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void mJSON_parser(Result rawResult) {
        try {
            JSONObject jsonObject = new JSONObject(Array_JSON.product_details_JSON);
            JSONObject data = jsonObject.getJSONObject("data");

            String productID = data.getString("product_id");
            String customer = data.getString("customer");
            String product_name = data.getString("product_name");
            String product_price = data.getString("product_price");
//            String product_imgUrl = data.getString("product_img_url");

            showMessageDialog("Contents = " + rawResult.getText() + ", Format = " + rawResult.getBarcodeFormat().toString(), productID, customer, product_name, product_price);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void mJSON_parser2(String product_name)
    {
        try {
            JSONObject jsonObject = new JSONObject(Array_JSON.product_details_JSON);
            JSONObject data = jsonObject.getJSONObject("data");

            String productID = data.getString("product_id");
            String customer = data.getString("customer");
//            String product_name = data.getString("product_name");
            String product_price = data.getString("product_price");
//            String product_imgUrl = data.getString("product_img_url");

            showMessageDialog2(productID, customer, product_name, product_price);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showMessageDialog2(String productID, String customer, String product_name, String product_price) {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        ScanerDilogFragmentActivity scanerDilogFragmentActivity = new ScanerDilogFragmentActivity(productID, customer, product_name, product_price);
        scanerDilogFragmentActivity.show(fragmentManager, "scannerDialogFragmentActivity");
    }


    /*    public void showMessageDialog(String message) {
            DialogFragment fragment = MessageDialogFragment.newInstance("Scan Results", message, this);
            fragment.show(getSupportFragmentManager(), "scan_results");
        }  */
    public void showMessageDialog(String message, String productID, String customer, String product_name, String product_price) {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        ScanerDilogFragmentActivity scanerDilogFragmentActivity = new ScanerDilogFragmentActivity(productID, customer, product_name, product_price);
        scanerDilogFragmentActivity.show(fragmentManager, "scannerDialogFragmentActivity");

/*        dialog_cross = new Dialog(FullScannerActivity.this);
        dialog_cross.setContentView(R.layout.scanner_dialog_frag_cross);
        dialog_cross.getWindow().setLayout(150, 150);
//        dialog_cross.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_cross.getWindow().setGravity(Gravity.BOTTOM);
        dialog_cross.show();
        dialog_cross.setCancelable(false);
        ImageButton imageButton_cross = dialog_cross.findViewById(R.id.imgBtn_scannerDialogCross);
        imageButton_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_cross.dismiss();
                scanerDilogFragmentActivity.dismiss();
                dialogDismissHandler();
            }
        });*/

    }

    public void closeMessageDialog() {
        closeDialog("scan_results");
    }

    public void closeFormatsDialog() {
        closeDialog("format_selector");
    }

    public void closeDialog(String dialogName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment fragment = (DialogFragment) fragmentManager.findFragmentByTag(dialogName);
        if (fragment != null) {
            fragment.dismiss();
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // Resume the camera
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onFormatsSaved(ArrayList<Integer> selectedIndices) {
        mSelectedIndices = selectedIndices;
        setupFormats();
    }

    @Override
    public void onCameraSelected(int cameraId) {
        mCameraId = cameraId;
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        if (mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<Integer>();
            for (int i = 0; i < ZXingScannerView.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }

        for (int index : mSelectedIndices) {
            formats.add(ZXingScannerView.ALL_FORMATS.get(index));
        }
        if (mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }


    @Override
    public void dataParsingMethod(boolean continueScanning, String productID, String productName, String productQuantity, String product_price, String totalBill) {
        //product save via Model
        invoiceItemList.add(new InvoiceItem(productName, Double.parseDouble(productQuantity), Double.parseDouble(product_price), Double.parseDouble(totalBill)));

        if (invoiceItemList.size() > 0) {
            Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
            relativeLayoutBottomSheetComponents.setVisibility(View.VISIBLE);
            linearLayoutBottomSheetCustomerName.setVisibility(View.VISIBLE);

            mAdapterHandler();
            totalBillHandler();
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED && !continueScanning)
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            mScannerView.resumeCameraPreview(this);
        } else {
            Toast.makeText(getApplicationContext(), "Failed to add product", Toast.LENGTH_SHORT).show();
/*            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED && !continueScanning)
            {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }*/
            mScannerView.resumeCameraPreview(this);
        }


        //Product save via sqlite
/*        long id = dbHelper.mAddSelectedProduct(productID, productName, productQuantity, product_price, totalBill);
        if (id < 0) {
            Toast.makeText(getApplicationContext(), "Failed to add product", Toast.LENGTH_SHORT).show();
*//*            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED && !continueScanning)
            {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }*//*
            mScannerView.resumeCameraPreview(this);
        } else {
            Toast.makeText(getApplicationContext(), "Successfully added product into db", Toast.LENGTH_SHORT).show();
            mAdapterHandler();
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED && !continueScanning)
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            mScannerView.resumeCameraPreview(this);
        }*/
    }

    @Override
    public void dialogDismissHandler() {
        mScannerView.resumeCameraPreview(this);
    }

    private void mAdapterHandler() {
//        recyclerViewHandler();
        if (invoiceItemList.size() > 0) {
            rvAdapter_selectedProductView = new RvAdapter_selectedProductView(invoiceItemList, this, this );
            rv_selectedProduct.setAdapter(rvAdapter_selectedProductView);
            rvAdapter_selectedProductView.notifyDataSetChanged();

        }
//        setupFormats();
//        contentFrame.addView(mScannerView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_flash_off:
            case R.id.imgBtn_flash_on:
                mFlash = !mFlash;
                if (mFlash) {
                    imageButtonFlashOn.setVisibility(View.VISIBLE);
                    imageButtonFlashOff.setVisibility(View.GONE);
                } else {
                    imageButtonFlashOn.setVisibility(View.GONE);
                    imageButtonFlashOff.setVisibility(View.VISIBLE);
                }
                mScannerView.setFlash(mFlash);
                break;
            case R.id.imgBtn_focus_on:
            case R.id.imgBtn_focus_off:
                mAutoFocus = !mAutoFocus;
                if (mAutoFocus) {
                    imageButtonFocusOn.setVisibility(View.VISIBLE);
                    imageButtonFocusOff.setVisibility(View.GONE);
                } else {
                    imageButtonFocusOn.setVisibility(View.GONE);
                    imageButtonFocusOff.setVisibility(View.VISIBLE);
                }
                mScannerView.setAutoFocus(mAutoFocus);
                break;

            case R.id.imgBtn_personSearch:
                dialog = new Dialog(FullScannerActivity.this);
                dialog.setContentView(R.layout.dialog_searchable_spinner_layout);

                WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.TOP | Gravity.LEFT;
                wmlp.x = 30;
                wmlp.y = 100;

                dialog.getWindow().setLayout(650, 800);
//                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


                EditText editText = dialog.findViewById(R.id.edtTxt_searchableText);
                ListView listView = dialog.findViewById(R.id.lv_searchableCustomerView);
                ImageButton imageButtonAddCustomer = dialog.findViewById(R.id.imgBtn_addCustomer);

                imageButtonAddCustomer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        dialogAddCustomer = new Dialog(FullScannerActivity.this);
                        dialogAddCustomer.setContentView(R.layout.dialog_add_customer_layout);

                        WindowManager.LayoutParams wmlp = dialogAddCustomer.getWindow().getAttributes();
                        wmlp.gravity = Gravity.TOP | Gravity.LEFT;
                        wmlp.x = 30;
                        wmlp.y = 100;

                        dialogAddCustomer.getWindow().setLayout(650, 700);
                        dialogAddCustomer.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogAddCustomer.show();
//                        dialogAddCustomer.setCancelable(false);

                        EditText editTextAddCustomer = dialogAddCustomer.findViewById(R.id.edtTxt_customerName);
                        Button buttonAddCustomer = dialogAddCustomer.findViewById(R.id.btn_addCustomer);

                        buttonAddCustomer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String customerName = editTextAddCustomer.getText().toString();
                                if (!TextUtils.isEmpty(customerName))
                                {
                                    textViewCustomerName.setText(customerName);
                                    customerArrayList.add(customerName);
                                    linearLayoutBottomSheetCustomerName.setVisibility(View.VISIBLE);

                                    if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)
                                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                                    dialogAddCustomer.dismiss();
                                }
                            }
                        });
                    }
                });

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(FullScannerActivity.this, R.layout.lv_customer_search_list, customerArrayList);

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
                        linearLayoutBottomSheetCustomerName.setVisibility(View.VISIBLE);
                        textViewCustomerName.setText(adapter.getItem(position));
//                        Toast.makeText(FullScannerActivity.this, adapter.getItem(position), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                break;

            case R.id.imgBtn_prodcutSearch:
                dialogProductSearch = new Dialog(FullScannerActivity.this);
                dialogProductSearch.setContentView(R.layout.dialog_product_search_layout);

                WindowManager.LayoutParams wmlp1 = dialogProductSearch.getWindow().getAttributes();
                wmlp1.gravity = Gravity.BOTTOM | Gravity.LEFT;
                wmlp1.x = 30;
                wmlp1.y = 230;

                dialogProductSearch.getWindow().setLayout(650, 800);
//                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialogProductSearch.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogProductSearch.show();
//                dialogProductSearch.setCancelable(false);

                EditText editText1 = dialogProductSearch.findViewById(R.id.edtTxt_productSearch);
                ListView listView1 = dialogProductSearch.findViewById(R.id.lv_searchableProductView);

                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(FullScannerActivity.this, R.layout.lv_customer_search_list, productArrayList);

                listView1.setAdapter(adapter1);

                editText1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //filter arrayList
                        adapter1.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String productName = adapter1.getItem(position);
                        mJSON_parser2(productName);
//                        Toast.makeText(FullScannerActivity.this, adapter.getItem(position), Toast.LENGTH_SHORT).show();
                        dialogProductSearch.dismiss();
                    }
                });
                break;
                /*case R.id.btn_nxtProductList:
                invoiceItemList = new ArrayList<>();
                invoiceItemList = dbHelper.invoiceItemTable();
                if (invoiceItemList.size() > 0) {
                    Intent invoiceActivity = new Intent(this, InvoiceActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable("selectedProductList", (Serializable) invoiceItemList);
                    invoiceActivity.putExtra("BUNDLE", args);
                    startActivity(invoiceActivity);
                } else
                    Toast.makeText(this, "No item found!", Toast.LENGTH_SHORT).show();
                break;*/


            //For InvoiceViewActivity
           /*     searchListItemList = new ArrayList<>();
                searchListItemList = dbHelper.showAllSelectedProductsInInvoiceActivity();
                if (searchListItemList.size() > 0) {
                    Intent invoiceActivity = new Intent(this, InvoiceViewerActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable("selectedProductList", (Serializable) searchListItemList);
                    invoiceActivity.putExtra("BUNDLE", args);
//                    invoiceActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    dbHelper.mDeleteProductList();
                    startActivity(invoiceActivity);
//                    Toast.makeText(this, "array.length", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, "No item found!", Toast.LENGTH_SHORT).show();
*/
        }
    }

    @Override
    public void onBackPressed() {
//        Intent mainActivity = new Intent(this, MainActivity.class);
//        mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(mainActivity);

        ScanerDilogFragmentActivity scanerDilogFragmentActivity = (ScanerDilogFragmentActivity) getSupportFragmentManager().findFragmentByTag("scannerDialogFragmentActivity");
        if (scanerDilogFragmentActivity != null && scanerDilogFragmentActivity.isVisible()) {
            scanerDilogFragmentActivity.dismiss();
            finish();
        } else
            super.onBackPressed();

    }

    @Override
    public void mCallBackTest() {
        mTest();
    }

    private void mTest() {
        mAdapterHandler();
        totalBillHandler();
    }
}