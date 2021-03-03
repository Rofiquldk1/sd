package com.agamilabs.smartshop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.activity.StockReportActivity;
import com.agamilabs.smartshop.controller.AppController;
import com.agamilabs.smartshop.model.AdminDashboardModel;
import com.agamilabs.smartshop.model.StockReportModel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public abstract class StockReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mCtx;
    private List<StockReportModel> mItemList;

    int mPageNumber = 1 ;


    public StockReportAdapter(Context mCtx, List<StockReportModel> mItemList) {
        this.mCtx = mCtx;
        this.mItemList = mItemList;

    }

    public void setPageNumber(int pageNumber){
        mPageNumber= pageNumber ;
    }
    public abstract void loadNextPage(int pageNumber) ;

    //product search
    public void searchFilterList(List<StockReportModel> filteredList) {
        mItemList = filteredList;
        notifyDataSetChanged();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.stock_report_list, null);
        return new StockReportAdapter.StockReportViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(position == (mItemList.size() - 1)){
//            AppController.getAppController().getInAppNotifier().log("position", position+"");
//            AppController.getAppController().getInAppNotifier().showToast("position:  "+ position);
            loadNextPage(mPageNumber+1);
        }
        StockReportModel mStockReport = mItemList.get(position);

        ((StockReportAdapter.StockReportViewHolder) holder).bind(mStockReport, position);

        AppController.getAppController().getInAppNotifier().log("response", "adapter");
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    class StockReportViewHolder extends RecyclerView.ViewHolder {

        TextView mId, mItemName, mInitialQty, mRemainingQty, mSaleRate, mPurchaseRate, mStockAmount, mReorder ;
        CircleImageView mImageLogo;


        public StockReportViewHolder(View itemView) {
            super(itemView);

//            mId = itemView.findViewById(R.id.text_id);
            mItemName = itemView.findViewById(R.id.text_item_name);
            mInitialQty = itemView.findViewById(R.id.text_initial_qty);
            mRemainingQty = itemView.findViewById(R.id.text_remaining_qty);
            mSaleRate = itemView.findViewById(R.id.text_sale_rate);
            mPurchaseRate = itemView.findViewById(R.id.text_purchase_rate);
            mStockAmount = itemView.findViewById(R.id.text_stock_amount);
            mReorder = itemView.findViewById(R.id.text_reorder_point);
            mImageLogo = itemView.findViewById(R.id.image_logo);

        }

        public void bind(StockReportModel mStockReport, int position) {

            BigDecimal decimal = new BigDecimal(mStockReport.getStockamount());

            String amount = String.format("%.2f", decimal) ;
            Log.e("TAG", "decimal:  "+ decimal ) ;
            Log.e("TAG", "amount1:  "+ amount ) ;
//            mId.setText(mStockReport.getItemno());
            mItemName.setText(mStockReport.getItemname());
            mItemName.setText(mStockReport.getItemname());
            mInitialQty.setText(mStockReport.getInitialqty());
            mInitialQty.setText( mStockReport.getInitialqty());
            mRemainingQty.setText(mStockReport.getRemainingqty());
            mSaleRate.setText(mStockReport.getSalerate());
            mPurchaseRate.setText(mStockReport.getPrate());
            mStockAmount.setText(String.valueOf(amount));
            mReorder.setText(mStockReport.getReorderpoint());



            if(Double.parseDouble(mStockReport.getRemainingqty()) < Integer.valueOf(mStockReport.getReorderpoint() )){
                mImageLogo.setBorderColor(Color.RED);
                mRemainingQty.setTextColor(Color.RED);
            } else if(Double.parseDouble(mStockReport.getRemainingqty()) < Integer.valueOf(mStockReport.getReorderpoint())*2   ){
                mImageLogo.setBorderColor(Color.YELLOW);
                mRemainingQty.setTextColor(Color.YELLOW);
            } else{
                mImageLogo.setBorderColor(Color.GREEN);
                mRemainingQty.setTextColor(Color.GREEN);
            }
        }
    }
}