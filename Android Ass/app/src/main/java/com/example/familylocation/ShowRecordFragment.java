package com.example.familylocation;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowRecordFragment extends Fragment {

    private FirebaseDatabase mData;
    private DatabaseReference mRef;
    private eventRecord eventR;
    private ListView listView;
    private ArrayList<eventRecord> eventList = new ArrayList<>();


    public ShowRecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_show_record, container, false);
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //FirebaseApp.initializeApp(getContext());
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String userUID = currentFirebaseUser.getUid();

        TextView nickName = (TextView) getView().findViewById(R.id.nickName);
        listView = (ListView) getView().findViewById(R.id.listViewSimple);
        mData = FirebaseDatabase.getInstance();
        mRef = mData.getReference("userEvent");
        Query query = mRef.orderByChild("uid").equalTo(userUID);
        eventList.clear();


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    eventR = ds.getValue(eventRecord.class);
                    String a = ds.child("nickName").getValue().toString();
                    eventList.add(eventR);
                    Log.d("onDataChange", "Hello: "+a);

                }
                listView.setAdapter(new EventAdapter(getContext(), R.layout.record_item, eventList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.d("setAdapter", "Hello: "+eventList.size());








    }

}
