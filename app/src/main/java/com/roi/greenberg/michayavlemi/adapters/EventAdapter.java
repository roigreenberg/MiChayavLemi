package com.roi.greenberg.michayavlemi.adapters;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.roi.greenberg.advancefirestorerecycleradapter.AdvanceFirestoreRecyclerAdapter;
import com.roi.greenberg.michayavlemi.R;
import com.roi.greenberg.michayavlemi.models.EventDetails;

import java.util.Date;

public class EventAdapter extends AdvanceFirestoreRecyclerAdapter<EventDetails, EventAdapter.EventHolder> {


    public EventAdapter(FirestoreRecyclerOptions<EventDetails> options, AppCompatActivity activity) {
        super(options, activity);
    }

    @Override
    protected void onBindViewHolder(@NonNull EventHolder viewHolder, int position, @NonNull EventDetails event) {
        viewHolder.bindItem(event, isSelected(position));

        viewHolder.itemView.setTag(position);

    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.event, parent, false);
        return new EventHolder(view);
    }

    public class EventHolder extends AdvanceFirestoreRecyclerAdapter.SelectableHolder{

        final TextView mTextViewEventName;
        final TextView mTextViewEventDay;
        final TextView mTextViewEventDate;
        final TextView mTextViewEventLocation;

        EventHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mTextViewEventName = itemView.findViewById(R.id.textViewEventName);
            mTextViewEventDay = itemView.findViewById(R.id.textViewEventDay);
            mTextViewEventDate = itemView.findViewById(R.id.textViewEventDate);
            mTextViewEventLocation = itemView.findViewById(R.id.textViewEventLocation);

        }

        void bindItem(EventDetails eventDetails, boolean isSelected){
            mTextViewEventName.setText(eventDetails.getName());
            mTextViewEventDay.setText(DateFormat.format("EEE", new Date(eventDetails.getTimeLong())));
            mTextViewEventDate.setText(DateFormat.format("dd/MM", new Date(eventDetails.getTimeLong())));
            mTextViewEventLocation.setText(eventDetails.getLocation());
        }

        @Override
        public void onClick(View v) {
            Log.d("EVENT", "onClick" + v.getTag().toString());
            int position = (int) v.getTag();
            String key = getSnapshots().getSnapshot(position).getId();

            Bundle bundle = new Bundle();
            bundle.putString("id", key);
            Navigation.findNavController(v).navigate(R.id.eventFragment, bundle);
        }

        @Override
        public boolean onLongClick(View v) {
            Log.d("EVENT", "onLongClick");
            return super.onLongClick(v);
        }
    }
}
