package com.agamilabs.smartshop.ui.storeadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.constants.AppServerURL;
import com.agamilabs.smartshop.controller.AppImageLoader;
import com.agamilabs.smartshop.ui.library.RoundRectCornerImageView;
import com.agamilabs.smartshop.ui.storeadmin.models.StoreProductModel;

import java.util.ArrayList;

public class StoreDashboardTopProductsAdapter extends RecyclerView.Adapter<StoreDashboardTopProductsAdapter.TopProductsViewHolder> {
    private Context context;
    private ArrayList<StoreProductModel> storeProductModelArrayList;

    public StoreDashboardTopProductsAdapter(Context context, ArrayList<StoreProductModel> storeProductModelArrayList) {
        this.context = context;
        this.storeProductModelArrayList = storeProductModelArrayList;
    }

    @NonNull
    @Override
    public TopProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TopProductsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_store_dashboard_top_product_recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TopProductsViewHolder holder, int position) {
        StoreProductModel storeProductModel = storeProductModelArrayList.get(position);

        if(storeProductModel.getStoreImageUrl()==null || storeProductModel.getStoreImageUrl().equalsIgnoreCase("null")){

                AppImageLoader.loadImageInView(storeProductModel.getSystemImageUrl(), holder.roundRectCornerImageView);

        }else{
            AppImageLoader.loadImageInView(storeProductModel.getStoreImageUrl(), holder.roundRectCornerImageView);
        }

        holder.productNameTextView.setText(storeProductModel.getProductName());
        holder.rateTextView.setText("" + storeProductModel.getRate()+" Tk.");
        holder.unitTextView.setText("Unit: " + storeProductModel.getUnitQty()+" "+storeProductModel.getUnitTextShort());
        holder.unitQtyTextView.setText("Unit Qty: " + storeProductModel.getUnitQty());
        holder.unitQtyTextView.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return storeProductModelArrayList.size();
    }

    class TopProductsViewHolder extends RecyclerView.ViewHolder{
        RoundRectCornerImageView roundRectCornerImageView;
        TextView productNameTextView, rateTextView, unitTextView, unitQtyTextView;

        public TopProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            roundRectCornerImageView = itemView.findViewById(R.id.store_dashboard_top_product_image);
            productNameTextView = itemView.findViewById(R.id.store_dashboard_top_product_name);
            rateTextView = itemView.findViewById(R.id.store_dashboard_top_product_price);
            unitTextView = itemView.findViewById(R.id.store_dashboard_top_customer_total_carts);
            unitQtyTextView = itemView.findViewById(R.id.store_dashboard_top_customer_total_amount);
        }
    }
}
