package com.agamilabs.smartshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.agamilabs.smartshop.adapter.RvAdapter_selectedProductDetailsView;
import com.agamilabs.smartshop.model.InvoiceModel;

import java.util.ArrayList;
import java.util.List;

public class InvoiceViewerActivity extends AppCompatActivity {
    private RecyclerView rv_selectedProductListView;
    private RvAdapter_selectedProductDetailsView rvAdapter_selectedProductDetailsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_viewer);

        rv_selectedProductListView = findViewById(R.id.rv_selectedProductListView);

//        rvHandler();
    }

    private void rvHandler() {
        rv_selectedProductListView.setHasFixedSize(true);
        rv_selectedProductListView.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");

//        List<SearchListItem> searchListItems = new ArrayList<>();
//        searchableDialog = new SearchableDialog(this, searchListItems, "Title");
        List<InvoiceModel> invoiceModelList = (ArrayList<InvoiceModel>) args.getSerializable("selectedProductList");
        rvAdapter_selectedProductDetailsView = new RvAdapter_selectedProductDetailsView(invoiceModelList, this);
        rv_selectedProductListView.setAdapter(rvAdapter_selectedProductDetailsView);
        rvAdapter_selectedProductDetailsView.notifyDataSetChanged();
    }
}