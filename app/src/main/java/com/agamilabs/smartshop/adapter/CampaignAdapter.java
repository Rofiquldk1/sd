package com.agamilabs.smartshop.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.agamilabs.smartshop.Interfaces.CallBack;
import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.model.CampaignItem;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class CampaignAdapter extends RecyclerView.Adapter<CampaignAdapter.CampaignViewHolder>  {
    private Context mContext;
    private ArrayList<CampaignItem> mCampaignList;
    private String schedule_no, message_title,message,catid,session,apiid,schedule_starting_datetime,schedule_particular_number,priority,status,schedule_entry_datetime,total_subscribers,total_sent_subscribers,total_cost;
    private CallBack callBack;
    int pageNumber=1;
    boolean mAllEnabled=true;

    public CampaignAdapter(Context mContext, ArrayList<CampaignItem> mCampaignList, CallBack callBack) {
        this.mContext = mContext;
        this.mCampaignList = mCampaignList;
        this.callBack=callBack;
    }

    public boolean isAllItemsEnabled(){ return mAllEnabled; }

    public void setAllItemsEnabled(boolean enable){
        mAllEnabled = enable;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public abstract void loadNextPage(int pageNumber);

    @NonNull
    @Override
    public CampaignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_campaign, parent, false);
        return new CampaignViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull CampaignViewHolder holder, int position) {

        if(position == mCampaignList.size()-1){
            loadNextPage(pageNumber+1);
        }

        CampaignItem currentItem = mCampaignList.get(position);

        schedule_no = currentItem.getSchedule_no();
        message_title = currentItem.getMessage_title();
        message = currentItem.getMessage();
        catid = currentItem.getCatid();
        session = currentItem.getSession();
        apiid = currentItem.getApiid();
        schedule_starting_datetime = currentItem.getSchedule_starting_datetime();
        schedule_particular_number = currentItem.getSchedule_particular_number();
        priority = currentItem.getPriority();
        status = currentItem.getStatus();
        schedule_entry_datetime = currentItem.getSchedule_entry_datetime();
        total_subscribers = currentItem.getTotal_subscribers();
        total_sent_subscribers = currentItem.getTotal_sent_subscribers();
        //total_cost = currentItem.getTotal_cost();

        int percentage = (int)((Integer.valueOf(total_sent_subscribers) * 100) / Integer.valueOf(total_subscribers));

        /*if(total_cost.isEmpty() || Double.valueOf(total_cost) <= 0.0){
            total_cost = "N/A";
        }*/

        if(apiid.equals("Regular")){
            holder.text_api_id.setText("Regular API");
        }
        else if(apiid.equals("Low Cost")){
            holder.text_api_id.setText("Turtel API");
        }

        if(status.equals("-1")){
            holder.btn_duplicate.setVisibility(View.VISIBLE);
            holder.linearLayout_message.setVisibility(View.GONE);
            holder.btn_pause.setVisibility(View.GONE);
            holder.btn_resume.setVisibility(View.INVISIBLE);
            holder.btn_suspend.setVisibility(View.INVISIBLE);

            holder.circleImageViewRed.setVisibility(View.VISIBLE);
            holder.circleImageViewGray.setVisibility(View.GONE);
            holder.circleImageViewGreen.setVisibility(View.GONE);
            holder.circleImageViewBlue.setVisibility(View.GONE);

        }
        else if(status.equals("0")){
            holder.btn_resume.setVisibility(View.VISIBLE);
            holder.btn_suspend.setVisibility(View.VISIBLE);
            holder.btn_duplicate.setVisibility(View.VISIBLE);
            holder.linearLayout_message.setVisibility(View.GONE);
            holder.btn_pause.setVisibility(View.GONE);

            holder.circleImageViewGray.setVisibility(View.VISIBLE);
            holder.circleImageViewGreen.setVisibility(View.GONE);
            holder.circleImageViewRed.setVisibility(View.GONE);
            holder.circleImageViewBlue.setVisibility(View.GONE);

        }
        else if(status.equals("1")){
            holder.btn_pause.setVisibility(View.VISIBLE);
            holder.btn_suspend.setVisibility(View.VISIBLE);
            holder.btn_duplicate.setVisibility(View.VISIBLE);
            holder.linearLayout_message.setVisibility(View.GONE);
            holder.btn_resume.setVisibility(View.GONE);

            holder.circleImageViewBlue.setVisibility(View.VISIBLE);
            holder.circleImageViewRed.setVisibility(View.GONE);
            holder.circleImageViewGray.setVisibility(View.GONE);
            holder.circleImageViewGreen.setVisibility(View.GONE);
        }
        else if(status.equals("2")){
            holder.btn_duplicate.setVisibility(View.VISIBLE);
            holder.linearLayout_message.setVisibility(View.GONE);
            holder.btn_pause.setVisibility(View.GONE);
            holder.btn_resume.setVisibility(View.INVISIBLE);
            holder.btn_suspend.setVisibility(View.INVISIBLE);

            holder.circleImageViewGreen.setVisibility(View.VISIBLE);
            holder.circleImageViewGray.setVisibility(View.GONE);
            holder.circleImageViewRed.setVisibility(View.GONE);
            holder.circleImageViewBlue.setVisibility(View.GONE);

        }
        else {
            holder.btn_duplicate.setVisibility(View.GONE);
            holder.btn_pause.setVisibility(View.GONE);
            holder.btn_resume.setVisibility(View.GONE);
            holder.btn_suspend.setVisibility(View.GONE);
            holder.linearLayout_message.setVisibility(View.VISIBLE);

            holder.circleImageViewRed.setVisibility(View.VISIBLE);
            holder.circleImageViewGreen.setVisibility(View.GONE);
            holder.circleImageViewGray.setVisibility(View.GONE);
            holder.circleImageViewBlue.setVisibility(View.GONE);

        }

        holder.text_message_title.setText(message_title);
        holder.text_schedule_start_date_time.setText("Start from : "+schedule_starting_datetime);
        //holder.text_total_cost.setText("Tk. "+total_cost);
        holder.text_message.setText(message);
        holder.text_total_subscribers.setText(total_subscribers);
        holder.text_progress.setText(percentage+"% completed");

        holder.progressBar.setMax(Integer.valueOf(total_subscribers));
        holder.progressBar.setProgress(Integer.valueOf(total_sent_subscribers));

        holder.btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onClickButton(0,schedule_no);
            }
        });
        holder.btn_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onClickButton(1,schedule_no);
            }
        });
        holder.btn_suspend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onClickButton(2,schedule_no);
            }
        });
        holder.btn_duplicate.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Do you want to duplicate this campaign?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                CampaignItem currentItem = mCampaignList.get(position);

                                schedule_no = currentItem.getSchedule_no();
                                message_title = currentItem.getMessage_title();
                                message = currentItem.getMessage();
                                catid = currentItem.getCatid();
                                session = currentItem.getSession();
                                apiid = currentItem.getApiid();
                                schedule_starting_datetime = currentItem.getSchedule_starting_datetime();
                                schedule_particular_number = currentItem.getSchedule_particular_number();
                                priority = currentItem.getPriority();
                                status = currentItem.getStatus();
                                schedule_entry_datetime = currentItem.getSchedule_entry_datetime();
                                total_subscribers = currentItem.getTotal_subscribers();
                                total_sent_subscribers = currentItem.getTotal_sent_subscribers();
                                //total_cost = currentItem.getTotal_cost();

                                callBack.duplicateInfo(
                                        schedule_no,message_title, message, catid, session, apiid, schedule_starting_datetime,  schedule_particular_number,  priority,  status,  schedule_entry_datetime,
                                         total_subscribers,  total_sent_subscribers);
                            }
                        });

                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });


        if(isAllItemsEnabled()){
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CampaignItem currentItem = mCampaignList.isEmpty() ? null : mCampaignList.get(position);

                    if (currentItem == null){
                        return;
                    }
                    message = currentItem.getMessage();

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage(message).setTitle("Message");
                    builder.setCancelable(true);

                    builder.setPositiveButton(
                            "Dismiss",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return mCampaignList.size();
    }


    public class CampaignViewHolder extends RecyclerView.ViewHolder {
        public TextView text_message_title,text_api_id,text_total_cost,text_msg_update,text_message,text_total_subscribers,text_schedule_start_date_time,text_progress;
        public Button btn_pause,btn_resume,btn_suspend,btn_duplicate;
        public ProgressBar progressBar;
        public LinearLayout linearLayout_message;
        public CardView cardView;
        public CircleImageView circleImageViewGreen,circleImageViewRed,circleImageViewGray,circleImageViewBlue;
        public CampaignViewHolder(@NonNull View itemView) {
            super(itemView);

            text_message_title = itemView.findViewById(R.id.text_msg_title);
            text_schedule_start_date_time = itemView.findViewById(R.id.text_start_date_time);
            text_api_id = itemView.findViewById(R.id.text_api_id);
            text_total_cost = itemView.findViewById(R.id.text_cost);
            text_message = itemView.findViewById(R.id.text_msg);
            text_total_subscribers = itemView.findViewById(R.id.text_total_subscribers);
            text_progress = itemView.findViewById(R.id.text_progress);
            text_msg_update = itemView.findViewById(R.id.text_msg_update);

            progressBar = itemView.findViewById(R.id.pBar);

            btn_pause = itemView.findViewById(R.id.btn_puse);
            btn_resume = itemView.findViewById(R.id.btn_resume);
            btn_suspend = itemView.findViewById(R.id.btn_suspend);
            btn_duplicate = itemView.findViewById(R.id.btn_duplicate);

            linearLayout_message = itemView.findViewById(R.id.layout_msg);

            cardView = itemView.findViewById(R.id.cardview_campaign);

            circleImageViewGreen = itemView.findViewById(R.id.img_active_green);
            circleImageViewRed = itemView.findViewById(R.id.img_active_red);
            circleImageViewGray = itemView.findViewById(R.id.img_active_gray);
            circleImageViewBlue = itemView.findViewById(R.id.img_active_blue);
        }
    }
}
