package com.agamilabs.smartshop.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.agamilabs.smartshop.controller.AppController;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppController.getAppController().getInAppNotifier().log("googleplay", "" + isGooglePlayAvailable());
    }

    public void checkPermission() {
        if (checkLocationPermission()) {
            startGPS();
            onLocationPermissionGranted();
        } else {
            // permission needed
            //onPermissionGranted();
            requestForLocationPermission();
        }
    }

    boolean isGooglePlayAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
            googleApiAvailability.getErrorDialog(this, resultCode, 1000).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (checkLocationPermission()) {
            startGPS();
        } else {
            // permission needed
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopGPS();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
//

    private final static int MY_PERMISSIONS_REQUEST_LOCATION = 101;
    private final static int MY_PERMISSIONS_REQUEST_CAMERA = 102;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            return false;
        } /*else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {


            return false;
        }*/ else {
            //Controller.getAppController().toastify("All permission granted");

            //onPermissionGranted();

            return true;
        }
    }

    public boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {


            requestForCameraPermission();

            return false;
        } else {
            //Controller.getAppController().toastify("All permission granted");

            //onPermissionGranted();
            onCameraPermissionGranted();

            return true;
        }
    }

    private void requestForLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    private void requestForCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }
    }

    void onPermissionGranted() {

    }

    void onLocationPermissionGranted() {

    }

    void onCameraPermissionGranted() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        //Controller.getAppController().toastify("location permission granted");

                        startGPS();

                        onLocationPermissionGranted();
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this.getClass() permission.
                    //Controller.getAppController().toastify("location permission denied");
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {
                        //Controller.getAppController().toastify("Camera permission granted");

                        //startGPS();
                        onCameraPermissionGranted();
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this.getClass() permission.
                    //Controller.getAppController().toastify("Camera permission denied");
                }
                return;
            }
        }
    }

    //private GPSService2 gpsService2;

    public void startGPS() {
        stopGPS();
//        gpsService2 = new GPSService2(this, this, MY_PERMISSIONS_REQUEST_LOCATION);
//        gpsService2.startLocationUpdates();
    }

    public void stopGPS() {
        //if (gpsService2 != null) {
            //gpsService2.stopLocationUpdates();
        //}
    }
}