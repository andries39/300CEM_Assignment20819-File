package com.example.familylocation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , BeaconConsumer {
    private FragmentManager mFragmentMgr;
    private LoginFragment loginFragment;
    private familyRecord familyRecord;
    private ShowRecordFragment showRecordFragment;
    private IbeaconFragment ibeaconFragment;
    private String userEmail;
    Runnable updater;
    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager;
    private String ibeacon001_id1 = "fda50693-a4e2-4fb1-afcf-c6eb07647855";
    private String ibeacon001_id2 = "1500";
    private String ibeacon001_id3 = "1500";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseApp.initializeApp(this);
        loginFragment =new LoginFragment();
        familyRecord = new  familyRecord();
        mFragmentMgr = getSupportFragmentManager();
        showRecordFragment = new ShowRecordFragment();
        ibeaconFragment = new IbeaconFragment();

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        mFragmentMgr.beginTransaction().replace(R.id.frame_container,loginFragment,"loginFragment").addToBackStack(null).commit();







        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);



        final Handler timerHandler = new Handler();
        updater = new Runnable() {
            @Override
            public void run() {

                NavigationView navigationView123 = (NavigationView) findViewById(R.id.nav_view);
                View headerView = navigationView123.getHeaderView(0);
                TextView showId  = (TextView) headerView.findViewById(R.id.userID);
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;


                if(currentFirebaseUser != null){
                    userEmail = currentFirebaseUser.getEmail();
                    showId.setText(userEmail);
                }else{
                    showId.setText(getString(R.string.txt_plzLogin));
                }
                timerHandler.postDelayed(updater,1000);
            }
        };
        timerHandler.post(updater);


        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String userUID = null;
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        if(currentFirebaseUser != null){
            userUID = currentFirebaseUser.getUid();
        }else {
            userUID = null;
        }



        if (id == R.id.nav_camera) {
            if(userUID == null){
                mFragmentMgr.beginTransaction().replace(R.id.frame_container,loginFragment,"loginFragment").addToBackStack(null).commit();
            }else{
                Toast.makeText(MainActivity.this,getString(R.string.toast_Already),Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_gallery) {
            if(userUID!=null){
                beaconManager.unbind(this);
                mFragmentMgr.beginTransaction().replace(R.id.frame_container,familyRecord,"familyRecord").addToBackStack(null).commit();
            }else{
                Toast.makeText(MainActivity.this,getString(R.string.toast_Please),Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_slideshow) {
            if(userUID!=null){
                beaconManager.unbind(this);
                mFragmentMgr.beginTransaction().replace(R.id.frame_container,showRecordFragment,"showRecordFragment").addToBackStack(null).commit();
            }else{
                Toast.makeText(MainActivity.this,getString(R.string.toast_Please),Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_manage) {
            if (userUID!=null){
                beaconManager.bind(this);
                mFragmentMgr.beginTransaction().replace(R.id.frame_container,ibeaconFragment,"ibeaconFragment").addToBackStack(null).commit();
            }else {
                Toast.makeText(MainActivity.this,getString(R.string.toast_Please),Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_share) {
            if(userUID!=null){
                beaconManager.unbind(this);
                FirebaseAuth.getInstance().signOut();
                mFragmentMgr.beginTransaction().replace(R.id.frame_container,loginFragment,"loginFragment").addToBackStack(null).commit();
                Toast.makeText(MainActivity.this,getString(R.string.toast_Logout),Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this,getString(R.string.toast_AlreadyOut),Toast.LENGTH_SHORT).show();
            }



        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.removeAllRangeNotifiers();

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {

                if (collection.size() > 0) {
                    List<Beacon> beacons = new ArrayList<>();
                    for (Beacon beacon : collection) {
                        beacons.add(beacon);
                    }
                    if (beacons.size() > 0) {
                        Collections.sort(beacons, new Comparator<Beacon>() {
                            public int compare(Beacon arg0, Beacon arg1) {
                                return arg1.getRssi()-arg0.getRssi();
                            }
                        });
                        Beacon nearBeacon = beacons.get(0);
                        if(nearBeacon.getId1().toString().equalsIgnoreCase(ibeacon001_id1)&& nearBeacon.getId2().toString().equalsIgnoreCase(ibeacon001_id2)&& nearBeacon.getId3().toString().equalsIgnoreCase(ibeacon001_id3)){
                            ibeaconFragment.setBeaconUID("beacon001");
                            Log.i(TAG, "find right ibeacon001 ");
                        }
                    }
                }else{
                    ibeaconFragment.setBeaconUID("");
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }

}
