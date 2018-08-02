package com.roi.greenberg.michayavlemi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.roi.greenberg.advancefirestorerecycleradapter.AdvanceFirestoreRecyclerAdapter;
import com.roi.greenberg.michayavlemi.activities.MainActivity;
import com.roi.greenberg.michayavlemi.adapters.ExpensesAdapter;
import com.roi.greenberg.michayavlemi.adapters.ItemAdapter;
import com.roi.greenberg.michayavlemi.fragments.AddNewItemFragment;
import com.roi.greenberg.michayavlemi.models.EventDetails;
import com.roi.greenberg.michayavlemi.models.Item;
import com.roi.greenberg.michayavlemi.models.UserInList;
import com.roi.greenberg.michayavlemi.utils.EventHandler;

import java.util.ArrayList;
import java.util.HashMap;

import static com.roi.greenberg.michayavlemi.utils.Constants.AVERAGE;
import static com.roi.greenberg.michayavlemi.utils.Constants.EVENTS;
import static com.roi.greenberg.michayavlemi.utils.Constants.ITEMS;
import static com.roi.greenberg.michayavlemi.utils.Constants.TIMESTAMP;
import static com.roi.greenberg.michayavlemi.utils.Constants.TOTAL_EXTENSES;
import static com.roi.greenberg.michayavlemi.utils.Constants.USERS;
import static com.roi.greenberg.michayavlemi.utils.Utils.initRecycleView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "id";

    private OnFragmentInteractionListener mListener;

    private static final String TAG = "EventActivity";
    private ArrayList<Item> mItems;
    //    private DatabaseReference mFirebaseDatabase, mEventRef;
    private DocumentReference mEventRef;
    private FirebaseFirestore mFirestoreDatabase;
    private ItemAdapter mItemAdapter;
    private ExpensesAdapter mExpensesAdapter;

    public String[] childRef = {"totalexpenses", "average"};
    public HashMap<String, ValueEventListener> childListeners = new HashMap<>();
    private FloatingActionButton fab;

    private EventHandler eventHandler;

    private TextView tvEventTotal;
    private TextView tvEventAvg;



    public EventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id Parameter 1.
     * @return A new instance of fragment EventFragment.
     */
    public static EventFragment newInstance(String id) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String eventId;

        if (getArguments() != null) {
            eventId = getArguments().getString(ARG_ID);
            if (eventId == null || eventId.isEmpty())
                return;
        } else {
            return;
        }

        eventHandler = EventHandler.getInstance();
        eventHandler.setEventId(eventId);


    }

    @Override
    public void onStart() {
        super.onStart();


        mEventRef.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() { // TODO NOT ACTIVITY!!
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Log.d(TAG, "onEvent");
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Log.d(TAG, "Current data: " + documentSnapshot.getData());
                    EventDetails eventDetails = documentSnapshot.toObject(EventDetails.class);
                    if (eventDetails == null) {
                        return;
                    }
                    eventHandler.setEventName(eventDetails.getName());
                    eventHandler.setNumOfUsers(eventDetails.getNumOfUsers());
                    eventHandler.setTotal(eventDetails.getTotalexpenses());
                    eventHandler.setAverage(eventDetails.getAverage());
                    tvEventTotal.setText(String.valueOf(eventHandler.getTotal()));
                    tvEventAvg.setText(String.valueOf(eventHandler.getAverage()));

                }
            }
        });

        Query itemsQuery = mEventRef.collection(ITEMS)
                .orderBy("type", Query.Direction.DESCENDING)
                .orderBy(TIMESTAMP, Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Item> ItemOptions = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(itemsQuery, Item.class)
                .setLifecycleOwner(this)
                .build();
        mItemAdapter = new ItemAdapter(ItemOptions, (MainActivity)getActivity());
        initRecycleView(getContext(), (RecyclerView) getView().findViewById(R.id.rv_event), mItemAdapter);

        Query expensesQuery = mEventRef.collection(USERS);
        FirestoreRecyclerOptions<UserInList> ExpensesOptions = new FirestoreRecyclerOptions.Builder<UserInList>()
                .setQuery(expensesQuery, UserInList.class)
                .setLifecycleOwner(this)
                .build();
        mExpensesAdapter = new ExpensesAdapter(ExpensesOptions);
        initRecycleView(getContext(), (RecyclerView) getView().findViewById(R.id.rv_event_bottom_sheet), mExpensesAdapter);


        final Button transactionsButton = getView().findViewById(R.id.bt_require_transactions);
        transactionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Navigation.findNavController(v).navigate(R.id.requireTransactionsFragment);
            }
        });

        EventFragment.EventActionModeCallback eventActionModeCallback = new EventFragment.EventActionModeCallback(getContext(), mItemAdapter, R.menu.selected_menu);
        mItemAdapter.setActionMode(eventActionModeCallback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event, container, false);

        fab = view.findViewById(R.id.fab_new_item);
        tvEventTotal = view.findViewById(R.id.rv_event_bottom_sheet_total);
        tvEventAvg = view.findViewById(R.id.rv_event_bottom_sheet_average);

        Log.d(TAG, "onCreate: " + eventHandler.getEventId());

//        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        if(mFirestoreDatabase == null) {
            mFirestoreDatabase = FirebaseFirestore.getInstance();
        }
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser != null) {
            eventHandler.setUserId(fUser.getUid());
            eventHandler.setUserName(fUser.getDisplayName());
        } else {
            eventHandler.setUserId("");
            eventHandler.setUserName("Anonymous");
        }

//        mEventRef = mFirebaseDatabase.child(EVENTS).child(mEventId);
        mEventRef = mFirestoreDatabase.collection(EVENTS).document(eventHandler.getEventId());


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                DialogFragment DateFragment = new AddNewItemFragment();
                DateFragment.show(fragmentManager, "addNewItem");
            }
        });

        View llBottomSheet = view.findViewById(R.id.content_event_bottom_sheet);

        // init the bottom sheet behavior
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class EventActionModeCallback extends AdvanceFirestoreRecyclerAdapter.SelectableActionModeCallback
    {
        private static final String TAG = "EventActionModeCallback";
        private AdvanceFirestoreRecyclerAdapter adapter;
        EventActionModeCallback(Context context, AdvanceFirestoreRecyclerAdapter adapter, int menu_layout) {
            super(context, adapter, menu_layout);
            this.adapter = adapter;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_delete:
                    // Get a new write batch
                    WriteBatch batch = mFirestoreDatabase.batch();
                    double price = 0;
                    for (int i = adapter.getItemCount() - 1; i >= 0; i--) {
                        if (adapter.isSelected(i)){
                            final QueryDocumentSnapshot doc = (QueryDocumentSnapshot) adapter.getSnapshots().getSnapshot(i);
                            String id = doc.getId();
                            batch.delete(mEventRef.collection(ITEMS).document(id));
                            price += doc.contains("price") ? doc.getDouble("price") : 0;
                        }
                    }
                    // Commit the batch
                    batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Delete success");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Delete failure.", e);
                        }
                    });
                    if (price > 0) {
                        FirebaseFirestore.getInstance().document(EVENTS + '/' + eventHandler.getEventId()).update(TOTAL_EXTENSES, eventHandler.getTotal() - price).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
                        FirebaseFirestore.getInstance().document(EVENTS + '/' + eventHandler.getEventId()).update(AVERAGE, eventHandler.getAverage() - (price / eventHandler.getNumOfUsers())).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
                    }


                    break;
                case R.id.menu_users_picker:
                    //                    showAssigningDialog(mode);
                    break;

                case R.id.menu_edit_item:
                    //                    int pos;
                    //                    final DatabaseReference itemRef;
                    //
                    //                    pos = (int) adapter.getSelectedItems().get(0);
                    //                    itemRef = adapter.getRef(pos).child("itemID");
                    //                    itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    //                        @Override
                    //                        public void onDataChange(DataSnapshot dataSnapshot) {
                    //                            User itemID = (User) dataSnapshot.getValue();
                    //                            Intent editItemIntent = new Intent(ListActivity.this, EditItemActivity.class);
                    //                            editItemIntent.putExtra("EXTRA_REF", itemID);
                    //                            startActivityForResult(editItemIntent, EDIT_REQUEST);
                    //                        }
                    //
                    //                        @Override
                    //                        public void onCancelled(DatabaseError databaseError) {
                    //
                    //                        }
                    //                  });
                    break;

                default:
                    return false;
            }
            mode.finish();
            return true;
        }

//        private void showAssigningDialog(final ActionMode mode) {
//
//            final CharSequence[] charSequenceItems = users.toArray(new CharSequence[users.size()]);
//            Log.d("RROI", charSequenceItems[0].toString());
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle("Assign items to:");
//            builder.setItems(charSequenceItems, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Log.e("RROI", "on picker");
//                    for (int i = adapter.getItemCount() - 1; i >= 0; i--) {
//                        ItemAdapter.ItemHolder itemHolder = (ItemAdapter.ItemHolder) adapter.getItem(i);
//                        if (adapter.isSelected(i)){
//                            Log.e("RROI", "Before " + adapter.getRef(i).child("assignee").toString());
//                            adapter.getRef(i).child("assignee").setValue(charSequenceItems[which].toString());
//                            adapter.notifyItemChanged(i);
//                            Log.e("RROI", "After " + adapter.getRef(i).child("assignee").toString());
//                        }
//                    }
//
//                    Log.d(TAG, "menu_users_picker");
//                    //itemAdapter.notifyDataSetChanged();
//                    //doneItemAdapter.notifyDataSetChanged();
//                    mode.finish();
//                }
//            });
//            AlertDialog b = builder.create();
//            b.show();
//        }
    }
}
