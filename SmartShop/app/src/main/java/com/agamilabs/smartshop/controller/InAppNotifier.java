package com.agamilabs.smartshop.controller;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class InAppNotifier {

    private Context context;

    private InAppNotifier(Context context) {
        this.context = context;
    }

    static InAppNotifier getInstance(Context context) {
        return new InAppNotifier(context);
    }

    private Toast toast = null;

    public void showToast(String text) {
        dismiss();
        toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }

    public void showToast(String text, int duration) {
        dismiss();
        toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void showToast(String text, int toastPosition1, int toastPosition2, int xPositionOffset, int yPositionOffset, int duration) {
        dismiss();
        toast = Toast.makeText(context, text, duration);
        toast.setGravity(toastPosition1 | toastPosition2, xPositionOffset, yPositionOffset);
        toast.show();
    }

    public void showToast(String text, int duration, View view) {
        dismiss();
        toast = Toast.makeText(context, text, duration);
        toast.setView(view);
        toast.show();
    }

    public void dismiss() {
        if (toast != null) {
            toast.cancel();
        }
    }

    public static final boolean DEBUG_ON = true;

    public void log(String tag, String msg) {
        if (DEBUG_ON)
            Log.e(tag + "", "msg: " + msg);
    }

}