package com.agamilabs.smartshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.model.NotifyModel;

import java.util.List;

public class AdminHomeFragmentAdapter extends RecyclerView.Adapter<AdminHomeFragmentAdapter.ShopAdminHomeViewHolder>
{
    private Context mCtx;
    private List<NotifyModel> toolsList;

    public AdminHomeFragmentAdapter(Context mCtx, List<NotifyModel> toolsList) {
        this.mCtx = mCtx;
        this.toolsList = toolsList;
    }



    @NonNull
    @Override
    public ShopAdminHomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.card_list, null);
        return new ShopAdminHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShopAdminHomeViewHolder holder, final int position) {

        NotifyModel faculty = toolsList.get(position);
        holder.textViewName.setText(faculty.getTitle());
//
//        final String Name = faculty.getName().toString().trim();

//        holder.textViewName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Context context = view.getContext();
//                Intent intent = new Intent(context, .class);
//                context.startActivity(intent);
//            }
//        });




    }

    @Override
    public int getItemCount() {
        return toolsList.size();
    }


    class ShopAdminHomeViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewName, textViewQuantity ;
        ImageView imageView ;




        public ShopAdminHomeViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_shop_name);
            textViewQuantity = itemView.findViewById(R.id.text_shop_quantity);
            imageView = itemView.findViewById(R.id.image_logo);




        }
    }




}