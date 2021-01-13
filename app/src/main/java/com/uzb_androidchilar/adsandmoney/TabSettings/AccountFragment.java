package com.uzb_androidchilar.adsandmoney.TabSettings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uzb_androidchilar.adsandmoney.R;

import java.util.HashMap;

public class AccountFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Query query;

    EditText mEmail, mPassword, mOldPass, mNewPass, mRepeatPass;
    Button activationLink, changeEmail, changePassword;
    String ab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        activationLink = view.findViewById(R.id.sendActivationLink);
        changeEmail = view.findViewById(R.id.changeEmail);
        changePassword = view.findViewById(R.id.changePassword);

        mEmail = view.findViewById(R.id.emailEt);
        mPassword = view.findViewById(R.id.passwordEt);
        mOldPass = view.findViewById(R.id.oldPassEt);
        mNewPass = view.findViewById(R.id.newPassEt);
        mRepeatPass = view.findViewById(R.id.repeatNewPassEt);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        query = databaseReference.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    ab = "" + ds.child("Email").getValue();
                    mEmail.setText(ab);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mEmail.addTextChangedListener(changeforEmail);
        mPassword.addTextChangedListener(changeforEmail);

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mEmail.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmail.setError("Invalid email");
                    mEmail.setFocusable(true);
                }
                else if (password.length() < 8){
                    mEmail.setError("Password length at least 8 chars");
                    mEmail.setFocusable(true);
                }
                else {
                    HashMap<String, Object> changeEmail = new HashMap<>();
                    changeEmail.put("Email", email);
                    databaseReference.child(user.getUid()).updateChildren(changeEmail)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });
                }
            }
        });


        return view;
    }

    private TextWatcher changeforEmail = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String newEmail = mEmail.getText().toString().trim();
            String newEmailForPass = mPassword.getText().toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                changeEmail.setEnabled(false);
            } else if (newEmailForPass.length() < 8) {
                changeEmail.setEnabled(false);
            } else {
                changeEmail.setEnabled(true);
                changeEmail.setBackgroundResource(R.drawable.earn_button_back);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}