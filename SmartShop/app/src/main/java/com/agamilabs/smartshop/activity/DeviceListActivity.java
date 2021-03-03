package com.agamilabs.smartshop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.adapter.AdminDashboardAdapter;
import com.agamilabs.smartshop.adapter.PrinterAdapter;
import com.agamilabs.smartshop.model.PrintersInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class DeviceListActivity extends AppCompatActivity {
    protected static final String TAG = "TAG";
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private RecyclerView recyclerView;
    private ArrayList<PrintersInfo> printers = new ArrayList<>();

    //Usb
    private UsbManager mUsbManager;
    private UsbDevice mDevice;
    private UsbDeviceConnection mConnection;
    private UsbInterface mInterface;
    private UsbEndpoint mEndPoint;
    private PendingIntent mPermissionIntent;
    EditText ed_txt;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private static Boolean forceCLaim = true;

    HashMap<String, UsbDevice> mDeviceList;
    Iterator<UsbDevice> mDeviceIterator;
    byte[] testBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_device_list);

        setResult(Activity.RESULT_CANCELED);

        loadMyRecyclerView();

        Intent intent = getIntent();
        if(intent.getStringExtra("type").equals("USB")){
            mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
            mDeviceList = mUsbManager.getDeviceList();
            mDeviceIterator = mDeviceList.values().iterator();
            if(mDeviceList.size() > 0){
                while (mDeviceIterator.hasNext()) {
                    UsbDevice usbDevice1 = mDeviceIterator.next();
                    PrintersInfo printersInfo = new PrintersInfo(
                            usbDevice1.getDeviceName(),
                            String.valueOf(usbDevice1.getDeviceId())
                    );
                    printers.add(printersInfo);
                }
            }
        }
        else  if(intent.getStringExtra("type").equals("Bluetooth")){
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter.getBondedDevices();

            if (mPairedDevices.size() > 0) {
                for (BluetoothDevice mDevice : mPairedDevices) {
                    PrintersInfo printersInfo = new PrintersInfo(
                            mDevice.getName(),
                            mDevice.getAddress()
                    );
                    printers.add(printersInfo);
                }
            } else {
                String mNoDevices = "None Paired";//getResources().getText(R.string.none_paired).toString();
            }
        }

    }

    private void loadMyRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PrinterAdapter adapter = new PrinterAdapter(printers,this);
        recyclerView.setAdapter(adapter);

    }

}