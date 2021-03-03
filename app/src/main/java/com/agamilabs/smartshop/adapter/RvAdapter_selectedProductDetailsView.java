package com.agamilabs.smartshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agamilabs.smartshop.model.InvoiceModel;
import com.agamilabs.smartshop.R;


import java.util.List;

public class RvAdapter_selectedProductDetailsView extends RecyclerView.Adapter<RvAdapter_selectedProductDetailsView.MyViewHolder> {
    private List<InvoiceModel> searchListItemList;
    private Context context;

    public RvAdapter_selectedProductDetailsView(List<InvoiceModel> searchListItemList, Context context) {
        this.searchListItemList = searchListItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_selected_product_details_view_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        InvoiceModel current = searchListItemList.get(position);
//        holder.tv_customer.setText(current.getCustomer());
//        holder.tv_date.setText(current.getDate());
//        holder.tv_productName.setText(current.getProductName());
//        holder.tv_productQuantity.setText(current.getProductQuantity());
//        holder.tv_productPrice.setText(current.getProductPrice());
//        holder.tv_discount.setText(current.getDiscount());
//        holder.tv_totalBill.setText(current.getTotalBill());
    }

    @Override
    public int getItemCount() {
        return searchListItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_invoiceNo, tv_customer, tv_date, tv_dueDate, tv_amount, tv_discount, tv_Status, tv_Action;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_invoiceNo = itemView.findViewById(R.id.tv_rvInvoiceViewer_invoiceNo);
            tv_customer = itemView.findViewById(R.id.tv_rvInvoiceViewer_customerName);
            tv_date = itemView.findViewById(R.id.tv_rvInvoiceViewer_date);
            tv_dueDate = itemView.findViewById(R.id.tv_rvInvoiceViewer_dueDate);
            tv_amount = itemView.findViewById(R.id.tv_rvInvoiceViewer_amount);
            tv_discount = itemView.findViewById(R.id.tv_rvInvoiceViewer_discount);
            tv_Status = itemView.findViewById(R.id.tv_rvInvoiceViewer_status);
            tv_Action = itemView.findViewById(R.id.tv_rvInvoiceViewer_action);
        }
    }
}

