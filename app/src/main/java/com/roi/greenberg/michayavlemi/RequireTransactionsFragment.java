package com.roi.greenberg.michayavlemi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.roi.greenberg.michayavlemi.adapters.TransactionsAdapter;
import com.roi.greenberg.michayavlemi.models.Transaction;
import com.roi.greenberg.michayavlemi.utils.EventHandler;

import static com.roi.greenberg.michayavlemi.utils.Constants.EVENTS;
import static com.roi.greenberg.michayavlemi.utils.Constants.TRANSACTIONS;
import static com.roi.greenberg.michayavlemi.utils.Utils.initRecycleView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RequireTransactionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RequireTransactionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequireTransactionsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private FirebaseFirestore mFirestoreDatabase;
    private TransactionsAdapter mTransactionsAdapter;

    public RequireTransactionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequireTransactionsFragment.
     */
    public static RequireTransactionsFragment newInstance(String param1, String param2) {

        return new RequireTransactionsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_require_transactions, container, false);

        EventHandler eventHandler = EventHandler.getInstance();
        mFirestoreDatabase = FirebaseFirestore.getInstance();
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
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
}
