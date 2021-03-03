package com.agamilabs.smartshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agamilabs.smartshop.InvoiceActivity;
import com.agamilabs.smartshop.model.InvoiceItem;
import com.agamilabs.smartshop.R;

import java.util.ArrayList;
import java.util.List;

public class RvAdapter_invoiceItemList extends RecyclerView.Adapter<RvAdapter_invoiceItemList.MyViewHolder> {
    private Context context;
    private List<InvoiceItem> invoiceItemList = new ArrayList<>();

    public RvAdapter_invoiceItemList(InvoiceActivity context, List<InvoiceItem> invoiceItemList) {
        this.context = context;
        this.invoiceItemList = invoiceItemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_invoice_item_list_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        InvoiceItem current = invoiceItemList.get(position);
        holder.textView_item.setText(current.getItem_name());
        holder.textView_rate.setText(String.valueOf((int) current.getUnit_price()));
        holder.textView_qty.setText(String.valueOf((int) current.getQty()));
        holder.textView_total.setText(String.valueOf((int) current.getItem_bill()));
    }

    @Override
    public int getItemCount() {
        return invoiceItemList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView textView_item, textView_rate, textView_qty, textView_total;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_item = itemView.findViewById(R.id.tv_rvInvoiceItemList_item);
            textView_rate = itemView.findViewById(R.id.tv_rvInvoiceItemList_rate);
            textView_qty = itemView.findViewById(R.id.tv_rvInvoiceItemList_quantity);
            textView_total = itemView.findViewById(R.id.tv_rvInvoiceItemList_total);
        }
    }
}
