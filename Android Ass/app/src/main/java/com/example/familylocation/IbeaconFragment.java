package com.example.familylocation;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import static org.altbeacon.beacon.service.BeaconService.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class IbeaconFragment extends Fragment{


    private DatabaseReference databaseReference;
    private Date currentTime;
    private Dialog dialog;
    private String ibeaconUID;


    public IbeaconFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ibeacon, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Button confirmBtn = (Button) getView().findViewById(R.id.confirmBtn);
        ibeaconUID = "";


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = ProgressDialog.show(getActivity(),
                        "讀取中", "請等待3秒...", true);
                Log.i("confirmBtnBefore", "try to run:"+ibeaconUID);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            Log.i("confirmBtn", "try to run:"+ibeaconUID);
                            if(ibeaconUID =="beacon001"){
                                Log.i("confirmBtn", "Running");

                                databaseReference = FirebaseDatabase.getInstance().getReference();
                                String id = databaseReference.push().getKey();
                                DatabaseReference newRef = databaseReference.child("userEvent").push();
                                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                                String userUID = currentFirebaseUser.getUid();
                                EditText nameEdt = (EditText) getView().findViewById(R.id.nameEdTxt);
                                String name = nameEdt.getText().toString().trim();


                                currentTime = Calendar.getInstance().getTime();
                                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String strDate = sdfDate.format(currentTime);

                                String event = "Home(confirm)";
                                String stringLat = "22.303141";
                                String stringLongt = "114.185876";


                                eventRecord eventRecord = new eventRecord(strDate,name,event,userUID,stringLat,stringLongt);
                                newRef.setValue(eventRecord);
                                Fragment showRecordFragment = new ShowRecordFragment();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame_container, showRecordFragment);
                                transaction.addToBackStack(null);

// Commit the transaction       //
                                transaction.commit();

                            }
                            Thread.sleep(3000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            dialog.dismiss();


                        }
                    }
                }).start();
                if(ibeaconUID =="beacon001"){
                    Toast.makeText(getContext(),getString(R.string.toast_submit),Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(),getString(R.string.toast_beacon),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setBeaconUID(String ibeaconUID){
        this.ibeaconUID = ibeaconUID;
    }


}



