package com.example.familylocation;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;
    private String userUID;
    public LoginFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        FirebaseApp.initializeApp(getContext());
        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(
                    @NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null) {

                    userUID =  user.getUid();
//
                }else{

                }
            }
        };
        Button loginBtn = (Button) getView().findViewById(R.id.loginButton);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = ((EditText)getView().findViewById(R.id.email))
                        .getText().toString();
                final String password = ((EditText)getView().findViewById(R.id.password))
                        .getText().toString();
                Log.d("AUTH", email+"/"+password);
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                Log.d("onComplete", "onComplete+usid: "+userUID);
                                Fragment welcomeFragment = new WelcomeFragment();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame_container, welcomeFragment);
                                transaction.addToBackStack(null);


                                transaction.commit();
                                if (!task.isSuccessful()){
                                    Log.d("onComplete", "登入失敗");
                                    register(email, password);
                                }else{

                                }
                            }
                        });
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authListener);
    }

    public void login(View v){

    }

    private void register(final String email, final String password) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.register_Title))
                .setMessage(getString(R.string.register_Message))
                .setPositiveButton(getString(R.string.register_okBtn), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createUser(email, password);
                    }
                })
                .setNeutralButton(getString(R.string.register_Cancel), null)
                .show();
    }

    private void createUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                String message =
                                            task.isComplete() ? getString(R.string.register_MessageDone) : getString(R.string.register_MessageFail);
                                new AlertDialog.Builder(getActivity())
                                        .setMessage(message)
                                        .setPositiveButton("OK", null)
                                        .show();
                            }
                        });
    }

}
