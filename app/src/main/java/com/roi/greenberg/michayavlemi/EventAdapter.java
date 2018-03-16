package com.roi.greenberg.michayavlemi;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;

public class EventAdapter extends SelectableAdapter<EventDetails, EventAdapter.EventHolder> {


    EventAdapter(Context context, FirebaseRecyclerOptions<EventDetails> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull EventHolder viewHolder, int position, @NonNull EventDetails event) {
        viewHolder.bindItem(event, isSelected(position));
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.event_item, parent, false);
        return new EventHolder(view);
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
