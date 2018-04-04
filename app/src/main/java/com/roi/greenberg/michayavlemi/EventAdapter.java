package com.roi.greenberg.michayavlemi;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.Date;

public class EventAdapter extends SelectableAdapter<EventDetails, EventAdapter.EventHolder> {


    EventAdapter(Context context, FirebaseRecyclerOptions<EventDetails> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull EventHolder viewHolder, int position, @NonNull EventDetails event) {
        viewHolder.bindItem(event, isSelected(position));

        viewHolder.itemView.setTag(position);
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.event, parent, false);
        return new EventHolder(view);
    }

    public class EventHolder extends SelectableAdapter.SelectableHolder{

        TextView mTextViewEventName;
        TextView mTextViewEventDay;
        TextView mTextViewEventDate;
        TextView mTextViewEventLocation;
        String key;

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
            mTextViewEventDay.setText(DateFormat.format("E", new Date(eventDetails.getTimeLong())));
            mTextViewEventDate.setText(DateFormat.format("MM/dd", new Date(eventDetails.getTimeLong())));
            mTextViewEventLocation.setText(eventDetails.getLocation());
        }

        @Override
        public void onClick(View v) {
            Log.d("EVENT", "onClick" + v.getTag().toString());
            int position = (int) v.getTag();
            String key = getRef(position).getKey();
            Intent eventIntent = new Intent(v.getContext(),EventActivity.class);
            eventIntent.putExtra("EXTRA_REF", key);
            v.getContext().startActivity(eventIntent);
        }

        @Override
        public boolean onLongClick(View v) {
            Log.d("EVENT", "onLongClick");
            return super.onLongClick(v);
        }
    }
}
