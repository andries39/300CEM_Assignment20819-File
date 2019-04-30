package com.example.familylocation;


import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class familyRecord extends Fragment {
    Button recordBtn,addCurrentLat,ref;
    EditText nickName;
    Spinner eventSp;
    DatabaseReference databaseReference;
    Date currentTime;
    FirebaseAuth auth;
    String userUID;
    double lat,longt;
    TextView showLocat;
    Runnable updater;


    public familyRecord() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_family_record, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        recordBtn =(Button)getView().findViewById(R.id.recordButton);
        addCurrentLat =(Button)getView().findViewById(R.id.addCurrentLat);

        nickName =(EditText) getView().findViewById(R.id.nickName);
        eventSp =(Spinner) getView().findViewById(R.id.event);
        showLocat = (TextView) getView().findViewById(R.id.showLocat);
        FirebaseApp.initializeApp(getContext());
        databaseReference = FirebaseDatabase.getInstance().getReference();


        MapsActivity mapsActivity = new MapsActivity();




        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        userUID = currentFirebaseUser.getUid();
        final Handler timerHandler = new Handler();
        updater = new Runnable() {
            @Override
            public void run() {
                String stringLat = getActivity().getSharedPreferences("test", MODE_PRIVATE)
                        .getString("lat", "");
                showLocat.setText(stringLat);
                timerHandler.postDelayed(updater,1000);
            }
        };
        timerHandler.post(updater);

        addCurrentLat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLocat.setText(getString(R.string.txt_saved));
                Intent intentMap = new Intent(getActivity(), MapsActivity.class);



                startActivity(intentMap);
            }
        });


        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEvent();
            }
        });




    }
    public void addEvent(){
        String name = nickName.getText().toString().trim();
        String event = eventSp.getSelectedItem().toString();

        //if(!name.equals(null)&& userUID.equals(null)){
            String id = databaseReference.push().getKey();
            DatabaseReference newRef = databaseReference.child("userEvent").push();

            currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = sdfDate.format(currentTime);
            String stringLat = getActivity().getSharedPreferences("test", MODE_PRIVATE)
                .getString("lat", "");
            String stringLongt = getActivity().getSharedPreferences("test", MODE_PRIVATE)
                .getString("longt", "");

            eventRecord eventRecord = new eventRecord(strDate,name,event,userUID,stringLat,stringLongt);
            //databaseReference.child(id).setValue(eventRecord);
            newRef.setValue(eventRecord);

            Toast.makeText(getContext(),getString(R.string.toast_submit),Toast.LENGTH_SHORT).show();

        //}else{
          //  Log.d("addEvent", "Failed to enter data!");
       // }
    }


}
