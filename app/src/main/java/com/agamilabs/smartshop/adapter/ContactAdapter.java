package com.agamilabs.smartshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.model.Contact;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>  {
    Context mContext;
    ArrayList<Contact> contacts;
    private ArrayList<String> phoneNumList = new ArrayList<>();
    String phoneNumber;

    public ContactAdapter(Context mContext, ArrayList<Contact> contacts) {
        this.mContext = mContext;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.contact_layout, parent, false);
        return new ContactAdapter.ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.text_name.setText(contact.getName());
        holder.text_phoneNumber.setText(contact.getPhoneNumber());

        holder.linearLayoutContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNum = contact.getPhoneNumber().trim();
                if(phoneNum.contains(" ")){
                    phoneNumber = phoneNum.replace(" ","").trim();
                }

                if(phoneNumber.length() > 11 && phoneNumber.trim().substring(0,3).equalsIgnoreCase("+88") &&
                        phoneNumber.substring(3,5).equalsIgnoreCase("01")){
                    String replace = phoneNumber.replace("+88","").trim();
                    if(replace.length() == 12 && replace.contains("-")){
                        String replace1 = replace.replace("-","");
                        if(!phoneNumList.contains(replace1)){
                            phoneNumList.add(replace1);
                        }
                    } else if(replace.length() == 11){
                        if(!phoneNumList.contains(replace)){
                            phoneNumList.add(replace);
                        }
                    }
                } else if(phoneNumber.length() == 11 && phoneNumber.substring(0,2).equalsIgnoreCase("01")){
                    if(!phoneNumList.contains(phoneNumber)){
                        phoneNumList.add(phoneNumber);
                    }
                } else if(phoneNumber.length() == 12 && phoneNumber.substring(0,2).equalsIgnoreCase("01")
                        && phoneNumber.contains("-")){
                    String replace1 = phoneNumber.replace("-","").trim();
                    if(!phoneNumList.contains(replace1)){
                        phoneNumList.add(replace1);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView text_name,text_phoneNumber;
        public LinearLayout linearLayoutContact;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.display_contactName);
            text_phoneNumber = itemView.findViewById(R.id.display_contactPhoneNumber);
            linearLayoutContact = itemView.findViewById(R.id.linear_layout_contact);
        }
    }
}

