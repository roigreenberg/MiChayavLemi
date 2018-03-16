package com.roi.greenberg.michayavlemi;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

        viewHolder.itemView.setTag(position);
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.event_item, parent, false);
        return new EventHolder(view);
    }

    public class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mTextViewEventName;
        TextView mTextViewEventDate;
        TextView mTextViewEventLocation;
        String key;

        EventHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTextViewEventName = itemView.findViewById(R.id.textViewEventName);
            mTextViewEventDate = itemView.findViewById(R.id.textViewEventDate);
            mTextViewEventLocation = itemView.findViewById(R.id.textViewEventLocation);

        }

        void bindItem(EventDetails eventDetails, boolean isSelected){
            mTextViewEventName.setText(eventDetails.getName());
            mTextViewEventDate.setText(String.valueOf(eventDetails.getDate()));
            mTextViewEventLocation.setText(eventDetails.getLocation());
        }

        @Override
        public void onClick(View v) {
            Log.d("EVENT", "here");
            int position = (int) v.getTag();
            String key = getRef(position).getKey();
            Intent toListOfItemsIntent = new Intent(v.getContext(),ItemListActivity.class);
            toListOfItemsIntent.putExtra("EXTRA_REF", key);
            v.getContext().startActivity(toListOfItemsIntent);
        }
    }
}
