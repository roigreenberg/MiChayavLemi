package com.roi.greenberg.michayavlemi.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.roi.greenberg.michayavlemi.MainFragment;
import com.roi.greenberg.michayavlemi.activities.EventActivity;
import com.roi.greenberg.michayavlemi.activities.MainActivity;
import com.roi.greenberg.michayavlemi.models.Item;
import com.roi.greenberg.michayavlemi.R;
import com.roi.greenberg.michayavlemi.models.User;
import com.roi.greenberg.michayavlemi.utils.EventHandler;
import com.roi.greenberg.michayavlemi.utils.Utils;

import java.util.ArrayList;

import static com.roi.greenberg.michayavlemi.utils.Constants.*;

/**
 * Created by greenberg on 19/03/2018.
 */

public class AddNewItemFragment extends DialogFragment {

    private EditText mItemName;
    private Spinner mUser;
    private EditText mPrice;
    private CheckBox mIsBought;
    private Switch mType;
    private ArrayList<User> allUsers;

    private EventHandler eventHandler;

    private static final String TAG = "AddNewItemFragment";

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d("ADDNEWEVENT", "onCreateDialog");
        final Context context = getContext();

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView = LayoutInflater.from(context).inflate(R.layout.dialog_add_new_item, null);
        mBuilder.setView(mView);
        mItemName = mView.findViewById(R.id.et_new_item_name);
        mUser = mView.findViewById(R.id.sp_new_item_user);
        mPrice = mView.findViewById(R.id.et_new_item_price);
        mIsBought = mView.findViewById(R.id.cb_new_item_isbought);
        mType = mView.findViewById(R.id.sw_new_item_type);

        eventHandler = EventHandler.getInstance();


        Query userNameQuery = FirebaseFirestore.getInstance()
                .collection(USERS).whereEqualTo(EVENTS + "." + eventHandler.getEventId(), true);
        allUsers = Utils.getUserNames(userNameQuery);


        Log.d("ADDITEM", allUsers.toString());

        ArrayAdapter<User> adapter = new ArrayAdapter<>(context,
                R.layout.simple_spinner_dropdown_item, allUsers);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setNotifyOnChange(false);
        mUser.setAdapter(adapter);


        mBuilder.setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                String name = mItemName.getText().toString();


                if (!name.isEmpty()) {
                    User user = (User) mUser.getSelectedItem();
                    String price_As_string = mPrice.getText().toString();
                    float price = 0;
                    if (!(price_As_string.isEmpty())) {
                        price = Float.valueOf(price_As_string);
                    }
                    FirebaseFirestore.getInstance()
                            .collection(EVENTS)
                            .document(eventHandler.getEventId())
                            .collection(ITEMS)
                            .document()
                            .set(new Item(name, MainFragment.mUser.getUid(), user.getUid(), price, mType.isChecked(), mIsBought.isChecked()), SetOptions.merge());
                    if (price > 0) {
                        FirebaseFirestore.getInstance().document(EVENTS + '/' + eventHandler.getEventId()).update(TOTAL_EXTENSES, eventHandler.getTotal() + price).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
                        FirebaseFirestore.getInstance().document(EVENTS + '/' + eventHandler.getEventId()).update(AVERAGE, eventHandler.getAverage() + (price / eventHandler.getNumOfUsers())).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
                    }
                } else {
                    Toast.makeText(context, "Please enter event name", Toast.LENGTH_SHORT).show();
                }
            }
        });


        final AlertDialog dialog = mBuilder.create();

        return dialog;
    }
}
