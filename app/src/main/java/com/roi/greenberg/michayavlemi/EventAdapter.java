package com.roi.greenberg.michayavlemi;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.Query;

public class EventAdapter extends SelectableAdapter<EventDetails, EventAdapter.EventHolder> {


    EventAdapter(Context context, Query query) {
        super(EventDetails.class, R.layout.event_item, EventAdapter.EventHolder.class, query);
    }

    @Override
    protected void populateViewHolder(EventHolder viewHolder, EventDetails event, int position) {
        viewHolder.bindItem(event, isSelected(position));
    }

    public static class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mTextViewEventName;
        TextView mTextViewEventDate;

        public EventHolder(View itemView) {
            super(itemView);

            mTextViewEventName = itemView.findViewById(R.id.textViewEventDay);
            mTextViewEventDate = itemView.findViewById(R.id.textViewEventDate);
        }

        void bindItem(EventDetails eventDetails, boolean isSelected){
            mTextViewEventName.setText(eventDetails.getNameEvent());
            mTextViewEventDate.setText(String.valueOf(eventDetails.getDate()));
        }

        @Override
        public void onClick(View v) {

        }
    }
}
