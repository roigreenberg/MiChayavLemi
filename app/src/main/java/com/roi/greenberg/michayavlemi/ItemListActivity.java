package com.roi.greenberg.michayavlemi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity implements ProductAdapter.ItemClickListener {
    private ArrayList<Product> mProducts;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference mFirebaseDatabaseItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        mFirebaseDatabaseItem = FirebaseDatabase.getInstance().getReference().child("events").child("items");

        mProducts = new ArrayList<>();
        mProducts = Product.generateDummyProductList();
        mFirebaseDatabaseItem.push().setValue(mProducts.get(0));

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("events")
                .child("items");


        initRecycleView();


    }

    /**
     * RecycleView initial
     */
    private void initRecycleView() {

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list_of_items);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);//list order
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ProductAdapter(mProducts, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(int clickItemIndex) {

    }
}
