package com.agamilabs.smartshop.ui.storeadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.controller.AppImageLoader;
import com.agamilabs.smartshop.ui.storeadmin.models.StoreTopCustomerModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoreDashboardTopCustomersAdapter extends RecyclerView.Adapter<StoreDashboardTopCustomersAdapter.TopCustomersViewHolder> {
    private Context context;
    private ArrayList<StoreTopCustomerModel> topCustomerModelArrayList;

    public StoreDashboardTopCustomersAdapter(Context context, ArrayList<StoreTopCustomerModel> topCustomerModelArrayList) {
        this.context = context;
        this.topCustomerModelArrayList = topCustomerModelArrayList;
    }

    @NonNull
    @Override
    public TopCustomersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TopCustomersViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_store_dashboard_top_customer_recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TopCustomersViewHolder holder, int position) {
        StoreTopCustomerModel storeTopCustomerModel = topCustomerModelArrayList.get(position);

        AppImageLoader.loadImageInView(storeTopCustomerModel.getImageUrl(), holder.circleImageView);

        String name = storeTopCustomerModel.getFirstName() + ((storeTopCustomerModel.getLastName() == null) ? "" : " " + storeTopCustomerModel.getLastName());
        holder.customerNameTextView.setText(name);

        if (storeTopCustomerModel.getContactNo().length() == 0) {
            holder.customerContactNoTextView.setVisibility(View.GONE);
        } else {
            holder.customerContactNoTextView.setVisibility(View.VISIBLE);
            holder.customerContactNoTextView.setText(storeTopCustomerModel.getContactNo());
        }

        holder.totalCartsTextView.setText(storeTopCustomerModel.getTotalCarts()+"");
        String amount = storeTopCustomerModel.getTotalAmount() + " Tk.";
        holder.totalAmountTextView.setText(amount);

    }

    @Override
    public int getItemCount() {
        return topCustomerModelArrayList.size();
    }

    class TopCustomersViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView customerNameTextView, customerContactNoTextView, totalCartsTextView, totalAmountTextView;

        public TopCustomersViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.store_dashboard_top_customer_image);
            customerNameTextView = itemView.findViewById(R.id.store_dashboard_top_customer_name);
            customerContactNoTextView = itemView.findViewById(R.id.store_dashboard_top_cutomer_contactno);
            totalCartsTextView = itemView.findViewById(R.id.store_dashboard_top_customer_total_carts);
            totalAmountTextView = itemView.findViewById(R.id.store_dashboard_top_customer_total_amount);
        }
    }
}
