package com.roi.greenberg.michayavlemi;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.roi.greenberg.michayavlemi.models.Item;

import java.util.ArrayList;
import java.util.List;

class SelectableAdapter<T, K extends SelectableAdapter.SelectableHolder> extends FirebaseRecyclerAdapter<T,K> {
    @SuppressWarnings("unused")
    private static final String TAG = SelectableAdapter.class.getSimpleName();

    private SparseBooleanArray selectedItems;
    private boolean mode;




    SelectableAdapter(FirebaseRecyclerOptions<T> options) {
        super(options);
        Log.d(TAG, "SelectableAdapter");
        selectedItems = new SparseBooleanArray();
        mode = true;
    }



    /**
     * Indicates if the item at position position is selected
     * @param position Position of the item to check
     * @return true if the item is selected, false otherwise
     */
    boolean isSelected(int position) {
        return getSelectedItems().contains(position);
    }

    /**
     * Toggle the selection status of the item at a given position
     * @param position Position of the item to toggle the selection status for
     */
    public void toggleSelection(int position) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    /**
     * Clear the selection status for all items
     */
    public void clearSelection() {
        List<Integer> selection = getSelectedItems();
        selectedItems.clear();
        for (Integer i : selection) {
            notifyItemChanged(i);
        }
    }

    /**
     * Count the selected items
     * @return Selected items count
     */
    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    /**
     * Indicates the list of selected items
     * @return SList of selected items ids
     */
    private List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); ++i) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    @Override
    protected void onBindViewHolder(@NonNull K holder, int position, @NonNull T model) {

        holder.setSelection(isSelected(position));

    }



    @NonNull
    @Override
    public K onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    public boolean isSelectedMode(){ return mode;}
    public void setSelectedMode(boolean mode) {this.mode = mode;}

    public void OnClick(int position, boolean isLong){
        Log.d(TAG, "On"+ (isLong? "Long" :"") +"Click pos " + position);
        if (isLong || isSelectedMode()) {
            toggleSelection(position);
        }


    }



    public class SelectableHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        public SelectableHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);


        }

        public void setSelection(boolean isSelected) {
            Log.d("ROIGR", "set selection " + isSelected);
            if (isSelected) {
                itemView.setBackgroundResource(R.color.selectedItem);
            } else {
                itemView.setBackgroundResource(R.color.transparent);
            }
        }

        public void bindItem() {

        }

        @Override
        public void onClick(View v) {
            OnClick(getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            OnClick(getAdapterPosition(), true);
            return true;
        }
    }
}
