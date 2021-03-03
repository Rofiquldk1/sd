package com.agamilabs.smartshop;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.agamilabs.smartshop.model.InvoiceModel;
import com.agamilabs.smartshop.Interfaces.ProductDetailsInterface;

public class ScanerDilogFragmentActivity extends DialogFragment implements View.OnClickListener {
    private Button button_addProduct, button_discard;
    private ImageButton button_productQuantityIncrease, button_productQuantityDecrease, button_productDiscountIncrease, button_productDiscountDecrease;
    private ImageView imageView_productImage;
    private InvoiceModel invoiceModel;
    private TextView tv_productName, tv_productQuantity, tv_productPrice, tv_productTotalPrice;
    private EditText editText_singleProdDiscount;
    private CheckBox checkBoxContinueScanning;

    private String massege;
    private String customer;
    private String date;
    private String product_name;
    private String product_quantity;
    private String product_price;
    private String product_discount;
    private String product_imgUrl;
    private String productID;
    private ProductDetailsInterface productDetailsInterface;
    private double totalPrice1;
    private double totalPrice2;
    private double totalPriceCommon;
    private double discount = 0;
    private boolean continueScanning;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        productDetailsInterface = (ProductDetailsInterface) getActivity();
    }

    public ScanerDilogFragmentActivity() {
    }

    ScanerDilogFragmentActivity(String productID, String customer, String product_name, String product_price) {
        this.customer = customer;
        this.product_name = product_name;
        this.product_price = product_price;
        this.productID = productID;
        totalPriceCommon = Double.parseDouble(product_price);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.scaner_dialog_fragment_final, null);

        button_addProduct = view.findViewById(R.id.btn_AddProductViewDialog);
        button_discard = view.findViewById(R.id.btn_dismissProductViewDialog);
        button_productQuantityIncrease = view.findViewById(R.id.btn_quantityIncrease);
        button_productQuantityDecrease = view.findViewById(R.id.btn_quantityDecrease);

        tv_productName = view.findViewById(R.id.tv_productName);
        tv_productQuantity = view.findViewById(R.id.tv_productQuantity);
        tv_productPrice = view.findViewById(R.id.tv_productPrice);
        tv_productTotalPrice = view.findViewById(R.id.tv_productTotalPrice);
        imageView_productImage = view.findViewById(R.id.imgV_productImage);
        editText_singleProdDiscount = view.findViewById(R.id.edtTxt_singleProductDiscount);
        checkBoxContinueScanning = view.findViewById(R.id.checkbox_continueScanning);

        setValueMethod();

        button_addProduct.setOnClickListener(this);
        button_discard.setOnClickListener(this);
        button_productQuantityIncrease.setOnClickListener(this);
        button_productQuantityDecrease.setOnClickListener(this);
/*        button_productDiscountIncrease.setOnClickListener(this);
        button_productDiscountDecrease.setOnClickListener(this);*/

//        discountHandler();

        setCancelable(true);
        return view;

    }

    private void discountHandler() {
        editText_singleProdDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString().trim()))
                {
                    discount = 0;
                    tv_productTotalPrice.setText("\u09F3"+String.valueOf(String.format("%.2f", totalPriceCommon)));
                } else {
                    discount = Double.parseDouble(s.toString());
                    double temp = totalPriceCommon - discount;
                    tv_productTotalPrice.setText("\u09F3"+String.valueOf(String.format("%.2f", temp)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

//    @Override
//    public void onDismiss(@NonNull DialogInterface dialog) {
//        super.onDismiss(dialog);
//        getActivity().finish();
//    }

    private void setValueMethod() {
        tv_productName.setText(product_name);
        tv_productQuantity.setText("1");
        tv_productPrice.setText("\u09F3"+ product_price);
        tv_productTotalPrice.setText("\u09F3"+totalPriceCommon);

//        tv_productDiscount.setText(product_discount);
//        tv_productPrice.setText(product_price);
//        Picasso.with(getContext()).load(product_imgUrl).into(imageView_productImage);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    productDetailsInterface.dialogDismissHandler();
                    dismiss();
                    return true;
                } else
                    return false;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dismissProductViewDialog:
                productDetailsInterface.dialogDismissHandler();
                dismiss();
                break;

            case R.id.btn_AddProductViewDialog:
                String productName = tv_productName.getText().toString();
                String productQuantity = tv_productQuantity.getText().toString();
//                String temp_totalBill = tv_productTotalPrice.getText().toString();
                String productDiscount = editText_singleProdDiscount.getText().toString();
                String totalBill = null;

                if (!TextUtils.isEmpty(productDiscount))
                {
                    if (Double.parseDouble(productDiscount) <= totalPriceCommon && Double.parseDouble(productDiscount) >= 0)
                    {
                        double temp = totalPriceCommon - Double.parseDouble(productDiscount);
                        totalBill = String.valueOf((int) temp);

                        if (checkBoxContinueScanning.isChecked()) {
                            continueScanning = true;
                        } else
                            continueScanning = false;

                        productDetailsInterface.dataParsingMethod(continueScanning, productID, productName, productQuantity, product_price, totalBill);

                        dismiss();
                    }
                    else {
                        Toast.makeText(getActivity(), "Please, fix your discount", Toast.LENGTH_SHORT).show();
                        productDetailsInterface.dialogDismissHandler();
                    }
                } else {
                    totalBill = String.valueOf(totalPriceCommon);
                    if (checkBoxContinueScanning.isChecked()) {
                        continueScanning = true;
                    } else
                        continueScanning = false;

                    productDetailsInterface.dataParsingMethod(continueScanning, productID, productName, productQuantity, product_price, totalBill);

                    dismiss();
                }


                break;

            case R.id.btn_quantityIncrease:
                int quantityNumber1 = Integer.parseInt(tv_productQuantity.getText().toString());
                quantityNumber1 = quantityNumber1 + 1;
                totalPrice1 = quantityNumber1 * Double.parseDouble(product_price);
                totalPriceCommon = totalPrice1;
                tv_productQuantity.setText(String.valueOf(quantityNumber1));
                tv_productTotalPrice.setText("\u09F3"+ String.format("%.2f", totalPriceCommon));
                break;

            case R.id.btn_quantityDecrease:
                int quantityNumber2 = Integer.parseInt(tv_productQuantity.getText().toString());
                quantityNumber2 = quantityNumber2 - 1;
                if (quantityNumber2 > 0)
                {
                    totalPrice2 = quantityNumber2 * Double.parseDouble(product_price);
                    totalPriceCommon = totalPrice2;
                    tv_productQuantity.setText(String.valueOf(quantityNumber2));
                    tv_productTotalPrice.setText("\u09F3"+ String.format("%.2f", totalPriceCommon));
                }

                break;


/*            case R.id.btn_discountIncrease:
                int discountNumber1 = Integer.parseInt(tv_productDiscount.getText().toString());
                discountNumber1 = discountNumber1 + 1;
                tv_productDiscount.setText(String.valueOf(discountNumber1));
                break;

            case R.id.btn_discountDecrease:
                int discountNumber2 = Integer.parseInt(tv_productDiscount.getText().toString());
                discountNumber2 = discountNumber2 - 1;
                tv_productDiscount.setText(String.valueOf(discountNumber2));
                break;*/
        }
    }


}
