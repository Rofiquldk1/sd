package com.agamilabs.smartshop.ui.storeadmin;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import androidx.annotation.FloatRange;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.ui.storeadmin.interfaces.IConfirmationCallback;

import java.util.Objects;

public class Util {
    public static int VIEW_ROW = 1;
    public static int VIEW_CARD = 2;

    public static String HTML_LINE_BREAK = "<br/>";

    public static String bodify(String input) {
        return "<b>" + input + "</b>";
    }

    public static String italify(String input) {
        return "<i>" + input + "</i>";
    }

    public static Spanned htmlToText(String input) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            return Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(input);
        }
    }

    public static final int CART_STATUS_DRAFT = 1;
    public static final int CART_STATUS_ORDER_PLACED = 2;
    public static final int CART_STATUS_REJECTED_BY_SHOP = 3;
    public static final int CART_STATUS_CONFIRMED_BY_SHOP = 4;
    public static final int CART_STATUS_REJECTED_BY_CUSTOMER = 5;
    public static final int CART_STATUS_CONFIRMED_BY_CUSTOMER = 6;
    public static final int CART_STATUS_ON_DELIVERY = 7;
    public static final int CART_STATUS_DELIVERED = 8;
    public static final int CART_STATUS_PAID = 9;


    public static final int[] ORDER_DAY_LIMIT = {1, 2, 7, 30, 90, 180, 365, 222222};
    public static final int[] ORDER_LIMIT = {1, 2, 7, 30, 90, 180, 365, 222222};

    public static int dpToPx(Context context, int dp) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public static String modify(int x, int minlength, String prefix) {
        if (prefix.length() == 0) {
            prefix = " ";
        }
        StringBuilder modified = new StringBuilder("" + x);

        for (int i = 0; i < (minlength - modified.toString().length()); i++) {
            modified.insert(0, prefix);
        }

        return modified.toString();
    }

    public static boolean isNull(String imageUrl) {
        return imageUrl == null || imageUrl.equalsIgnoreCase("null") || imageUrl.length() == 0;
    }

    public static void showOnDialog(Activity activity, String title, String text, String confirmText, String cancelText,
                              final IConfirmationCallback iConfirmationCallback) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(activity));

        TextView textView = new TextView(activity);
        textView.setText(Util.htmlToText(text));
        textView.setTextColor(ContextCompat.getColor(activity, R.color.black));
        textView.setPadding(Util.dpToPx(activity, 20), Util.dpToPx(activity, 10),
                Util.dpToPx(activity, 20), Util.dpToPx(activity, 10));

        alertDialogBuilder.setTitle(title);
//        alertDialogBuilder.setMessage(text);
        alertDialogBuilder.setView(textView);
        alertDialogBuilder.setPositiveButton(confirmText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (iConfirmationCallback != null) {
                    iConfirmationCallback.onConfirm();
                }
            }
        });
        alertDialogBuilder.setNegativeButton(cancelText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (iConfirmationCallback != null) {
                    iConfirmationCallback.onReject();
                }
            }
        });
        alertDialogBuilder.show();
    }
}
