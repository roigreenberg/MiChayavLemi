package com.roi.greenberg.michayavlemi.utils;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class Utils {

    /**
     * RecycleView initial
     */
    public static <A extends FirebaseRecyclerAdapter, C> void initRecycleView(Context context, RecyclerView view, A adapter) {
        RecyclerView mRecyclerView = view;
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
//        mLayoutManager.setReverseLayout(true);//list order
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter.startListening();
        mRecyclerView.setAdapter(adapter);
    }
}
