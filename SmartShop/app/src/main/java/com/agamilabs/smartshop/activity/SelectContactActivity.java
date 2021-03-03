package com.agamilabs.smartshop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.adapter.ContactAdapter;
import com.agamilabs.smartshop.model.Contact;

import java.util.ArrayList;

public class SelectContactActivity extends AppCompatActivity {
    private RecyclerView rvContacts;
    private ContactAdapter contactAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);

        ArrayList<Contact> contacts = getContacts();

        rvContacts = findViewById(R.id.recycler_view_contacts);
        rvContacts.setHasFixedSize(true);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter = new ContactAdapter(this,contacts);
        rvContacts.setAdapter(contactAdapter);
    }

    public ArrayList<Contact> getContacts() {
        // Retrieve Contacts from phone
        Cursor result = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

        // Create list of contact objects
        ArrayList<Contact> contact = new ArrayList<Contact>();

        // For number of contacts create a contact object with name and number.
        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            contact.add(new Contact(result.getString(result.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)), result.getString(result.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))));
        }
        return contact;
    }
}