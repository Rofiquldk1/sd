package com.agamilabs.smartshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.model.PrintersInfo;

import java.util.ArrayList;

import javax.xml.namespace.QName;

public class PrinterAdapter extends RecyclerView.Adapter<PrinterAdapter.ViewHolder> {
    private ArrayList<PrintersInfo> printers = new ArrayList<>();
    private Context context;

    public PrinterAdapter(ArrayList<PrintersInfo> printers, Context context) {
        this.printers = printers;
        this.context = context;
    }


    @NonNull
    @Override
    public PrinterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_printers,parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrinterAdapter.ViewHolder holder, int position) {
        holder.name.setText(printers.get(position).getPrinterName().toString());
        holder.address.setText(printers.get(position).getPrinterAddress().toString());
    }

    @Override
    public int getItemCount() {
        return printers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,address;
        public ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.txt_name);
            address = view.findViewById(R.id.txt_address);
        }
    }
}
