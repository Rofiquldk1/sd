package com.agamilabs.smartshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agamilabs.smartshop.FullScannerActivity;
import com.agamilabs.smartshop.Interfaces.IcallBackTest;
import com.agamilabs.smartshop.model.InvoiceItem;
import com.agamilabs.smartshop.model.InvoiceModel;
import com.agamilabs.smartshop.R;
import com.google.android.gms.common.internal.StringResourceValueReader;


import java.util.List;

public class RvAdapter_selectedProductView extends RecyclerView.Adapter<RvAdapter_selectedProductView.MyViewHolder> {
    private List<InvoiceItem> invoiceItemList;
    private Context context;
    private IcallBackTest icallBackTest;

    public RvAdapter_selectedProductView(List<InvoiceItem> invoiceItemList, Context context, IcallBackTest icallBackTest) {
        this.invoiceItemList = invoiceItemList;
        this.context = context;
        this.icallBackTest = icallBackTest;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_selected_product_view_layout, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        InvoiceItem current = invoiceItemList.get(position);

        holder.tv_productName.setText(current.getItem_name());
        holder.tv_productQuantity.setText(String.valueOf((int) current.getQty()));
        holder.tv_productPrice.setText("\u09F3" + String.format("%.2f", current.getUnit_price()));
        holder.tv_productTotalPrice.setText("\u09F3" + String.format("%.2f", current.getItem_bill()));


    }

    @Override
    public int getItemCount() {
        return invoiceItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_productName, tv_productQuantity, tv_productPrice, tv_productTotalPrice;
        ImageButton imageButtonQuantityIncr, imageButtonQuantityDecr, imageButtonDeleteItem;
        double itemBillCommon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_productName = itemView.findViewById(R.id.tv_rvProductView_productName);
            tv_productQuantity = itemView.findViewById(R.id.tv_rvProductView_productQuantity);
            tv_productTotalPrice = itemView.findViewById(R.id.tv_rvProductView_productTotalPrice);
            tv_productPrice = itemView.findViewById(R.id.tv_rvProductView_productPrice);
            imageButtonQuantityIncr = itemView.findViewById(R.id.btn_rvProductView_quantityIncrease);
            imageButtonQuantityDecr = itemView.findViewById(R.id.btn_rvProductView_quantityDecrease);
            imageButtonDeleteItem = itemView.findViewById(R.id.imgBtn_rvProductView_productDelete);

            imageButtonQuantityDecr.setOnClickListener(this);
            imageButtonQuantityIncr.setOnClickListener(this);
            imageButtonDeleteItem.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onClick(View v) {
            InvoiceItem current = invoiceItemList.get(getAdapterPosition());

            double unitPrice = current.getUnit_price();
            double itemBill = current.getItem_bill();
            double itemQty = current.getQty();
            double itemDiscount = (unitPrice * itemQty) - itemBill;
            switch (v.getId()) {
                case R.id.btn_rvProductView_quantityDecrease:
                    double qty = Double.parseDouble(tv_productQuantity.getText().toString());
                    qty--;
                    if (qty > 0) {
                        itemBillCommon = (qty * unitPrice) - itemDiscount;
                        current.setItem_bill(itemBillCommon);
                        current.setQty(qty);
                        tv_productQuantity.setText(String.valueOf((int) qty));
                        tv_productTotalPrice.setText("\u09F3" + String.format("%.2f", itemBillCommon));
                    }
                    icallBackTest.mCallBackTest();
                    break;

                case R.id.btn_rvProductView_quantityIncrease:
                    double qty2 = Double.parseDouble(tv_productQuantity.getText().toString());
                    qty2++;
                    if (qty2 > 0) {
                        itemBillCommon = (qty2 * unitPrice) - itemDiscount;
                        current.setItem_bill(itemBillCommon);
                        current.setQty(qty2);
                        tv_productQuantity.setText(String.valueOf((int) qty2));
                        tv_productTotalPrice.setText("\u09F3" + String.format("%.2f", itemBillCommon));
                        icallBackTest.mCallBackTest();
                    }
                    break;

                case R.id.imgBtn_rvProductView_productDelete:
                    invoiceItemList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), invoiceItemList.size());
                    icallBackTest.mCallBackTest();
                    break;
            }
        }
    }


}
