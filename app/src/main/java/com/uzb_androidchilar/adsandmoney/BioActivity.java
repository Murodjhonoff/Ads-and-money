package com.uzb_androidchilar.adsandmoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class BioActivity extends AppCompatActivity {
    private static final int TIME_OUT = 2000;
    private TextView signIn;
    private EditText mFirstName, mLastName;
    private Button mRegisterBtn;
    private CheckBox female, male;

    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        signIn = findViewById(R.id.sign_in_bio);
        mFirstName = findViewById(R.id.firstNameEtBio);
        mLastName = findViewById(R.id.lastNameEtBio);
        mRegisterBtn = findViewById(R.id.registerBtnOwn);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering...");

        female = findViewById(R.id.female);
        male = findViewById(R.id.male);

        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    male.setChecked(false);
                    String female = "Female";
                    HashMap<String, Object> updateEarn = new HashMap<>();
                    updateEarn.put("Gender", female);

                    databaseReference.child(user.getUid()).updateChildren(updateEarn)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });
                }
            }
        });
        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    female.setChecked(false);
                    String male = "Male";
                    HashMap<String, Object> updateEarn = new HashMap<>();
                    updateEarn.put("Gender", male);

                    databaseReference.child(user.getUid()).updateChildren(updateEarn)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });
                }
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName = mFirstName.getText().toString().trim();
                String lastName = mLastName.getText().toString().trim();

                if (firstName.length() < 2 || Patterns.PHONE.matcher(firstName).matches()) {
                    mFirstName.setError("Enter your full name");
                    mFirstName.setFocusable(true);
                } else if (lastName.length() < 2 || Patterns.PHONE.matcher(firstName).matches()) {
                    mLastName.setError("Enter your full name");
                    mLastName.setFocusable(true);
                } else {
                    progressDialog.show();
                    HashMap<String, Object> saveOwnData = new HashMap<>();
                    saveOwnData.put("First Name", firstName);
                    saveOwnData.put("Last Name", lastName);

                    databaseReference.child(user.getUid()).updateChildren(saveOwnData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            describAct();
                                        }
                                    }, TIME_OUT);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(BioActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BioActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    private void describAct() {
        startActivity(new Intent(BioActivity.this, DescriptionActivity.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}