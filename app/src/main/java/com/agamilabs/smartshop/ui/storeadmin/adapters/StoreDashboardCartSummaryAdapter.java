package com.agamilabs.smartshop.ui.storeadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.ui.storeadmin.models.StoreDashboardCartSummaryModel;

import java.util.ArrayList;

public class StoreDashboardCartSummaryAdapter extends RecyclerView.Adapter<StoreDashboardCartSummaryAdapter.StoreDashboardCartSummaryViewHolder> {
    private Context context;
    private ArrayList<StoreDashboardCartSummaryModel> dashboardCartSummaryModelArrayList;

    public StoreDashboardCartSummaryAdapter(Context context, ArrayList<StoreDashboardCartSummaryModel> dashboardCartSummaryModelArrayList) {
        this.context = context;
        this.dashboardCartSummaryModelArrayList = dashboardCartSummaryModelArrayList;
    }

    @NonNull
    @Override
    public StoreDashboardCartSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StoreDashboardCartSummaryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_store_dashboard_cart_summary_recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StoreDashboardCartSummaryViewHolder holder, int position) {
        StoreDashboardCartSummaryModel storeDashboardCartSummaryModel = dashboardCartSummaryModelArrayList.get(position);

        holder.summaryTitleTextView.setText(storeDashboardCartSummaryModel.getSummaryItemTitle());
        holder.totalCartsTextView.setText(storeDashboardCartSummaryModel.getTotalCarts()+" Orders");
        holder.totalAmountTextView.setText("Sell: " + storeDashboardCartSummaryModel.getTotalAmounts()+" Tk.");
    }

    @Override
    public int getItemCount() {
        return dashboardCartSummaryModelArrayList.size();
    }

    class StoreDashboardCartSummaryViewHolder extends RecyclerView.ViewHolder{
        TextView summaryTitleTextView, totalCartsTextView, totalAmountTextView;

        public StoreDashboardCartSummaryViewHolder(@NonNull View itemView) {
            super(itemView);

            summaryTitleTextView = itemView.findViewById(R.id.store_dashboard_cart_summary_item_title);
            totalCartsTextView = itemView.findViewById(R.id.store_dashboard_cart_summary_item_total_carts);
            totalAmountTextView = itemView.findViewById(R.id.store_dashboard_cart_summary_item_total_amount);

        }
    }

}
