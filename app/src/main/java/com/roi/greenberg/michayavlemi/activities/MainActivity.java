package com.roi.greenberg.michayavlemi.activities;

import android.content.DialogInterface;
import androidx.fragment.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.roi.greenberg.michayavlemi.MainFragment;
import com.roi.greenberg.michayavlemi.R;
import com.roi.greenberg.michayavlemi.adapters.EventAdapter;
import com.roi.greenberg.michayavlemi.fragments.AddNewEventFragment;
import com.roi.greenberg.michayavlemi.models.EventDetails;
import com.roi.greenberg.michayavlemi.models.User;

import java.util.Arrays;
import java.util.List;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.roi.greenberg.michayavlemi.models.UserInList;
//import com.roi.greenberg.selectablefirebaserecycleradapter.SelectableFirebaseRecyclerAdapter;

import static com.roi.greenberg.michayavlemi.utils.Constants.*;
import static com.roi.greenberg.michayavlemi.utils.Utils.updateLong;

public class MainActivity extends AppCompatActivity implements LifecycleOwner {

    private static final String ANONYMOUS = "anonymous";
    private static final String TAG = "MainActivity";

//    private RecyclerView mRecyclerView;
//    private List<EventDetails> mEventDetails;
//    private EventAdapter mEventAdapter;
//    private static FirebaseDatabase mFirebaseDatabase;
//    private static DatabaseReference mDatabaseReference;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (mFirebaseDatabase == null) {
//            mFirebaseDatabase = FirebaseDatabase.getInstance();
//            mFirebaseDatabase.setPersistenceEnabled(true);
//            mDatabaseReference = mFirebaseDatabase.getReference();
//        }

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .setPersistenceEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);


//        mContext = this;


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
    }




    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
    * Callback invoked when a menu item was selected from this Activity's menu.
    *
    * @param item The menu item that was selected by the user
    *
    * @return true if you handle the menu click here, false otherwise
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:
//            startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
