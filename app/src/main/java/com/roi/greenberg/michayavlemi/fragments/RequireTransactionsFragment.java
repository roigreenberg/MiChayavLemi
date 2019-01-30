package com.roi.greenberg.michayavlemi.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.roi.greenberg.michayavlemi.R;
import com.roi.greenberg.michayavlemi.adapters.TransactionsAdapter;
import com.roi.greenberg.michayavlemi.models.Transaction;
import com.roi.greenberg.michayavlemi.utils.DatabaseHandler;
import com.roi.greenberg.michayavlemi.utils.EventHandler;

import static com.roi.greenberg.michayavlemi.utils.Constants.EVENTS;
import static com.roi.greenberg.michayavlemi.utils.Constants.TRANSACTIONS;
import static com.roi.greenberg.michayavlemi.utils.Utils.initRecycleView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {link RequireTransactionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RequireTransactionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequireTransactionsFragment extends Fragment {

    private static final String TAG = "RequireTransactionsFrag";

    private TransactionsAdapter mTransactionsAdapter;

    private DatabaseHandler databaseHandler;
    private EventHandler eventHandler;
    //    private OnFragmentInteractionListener mListener;
    private FirebaseFirestore mFirestoreDatabase;

    public RequireTransactionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RequireTransactionsFragment.
     */
    private static RequireTransactionsFragment newInstance() {

        return new RequireTransactionsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHandler = DatabaseHandler.getInstance();
        eventHandler = EventHandler.getInstance();
        //    private OnFragmentInteractionListener mListener;
        mFirestoreDatabase = FirebaseFirestore.getInstance();

        databaseHandler.calculateTransactions(eventHandler.getEventId())
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                                Object details = ffe.getDetails();
                                Log.e(TAG, code.toString() + ", " + details.toString());
                            }

                            // ...
                        }

                        // ...
                    }
                });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_require_transactions, container, false);


        Query transactionQuery = mFirestoreDatabase.collection(EVENTS).document(eventHandler.getEventId()).collection(TRANSACTIONS);
//        Query transactionQuery = mFirebaseDatabase.child("events").child(mEventId).child("require_transactions");
        FirestoreRecyclerOptions<Transaction> transactionOptions = new FirestoreRecyclerOptions.Builder<Transaction>()
                .setQuery(transactionQuery, Transaction.class)
                .build();
        mTransactionsAdapter = new TransactionsAdapter(transactionOptions);
        initRecycleView(getContext(), (RecyclerView) view.findViewById(R.id.rv_transaction), mTransactionsAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTransactionsAdapter.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();

        mTransactionsAdapter.stopListening();
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
////        if (context instanceof OnFragmentInteractionListener) {
////            mListener = (OnFragmentInteractionListener) context;
////        } else {
////            throw new RuntimeException(context.toString()
////                    + " must implement OnFragmentInteractionListener");
////        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
