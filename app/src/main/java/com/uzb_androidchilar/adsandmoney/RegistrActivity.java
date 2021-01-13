package com.uzb_androidchilar.adsandmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrActivity extends AppCompatActivity {
    private TextView signIn;
    private EditText mEmailEt, mPasswordEt;
    private Button mRegisterBtn;

    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registr);

        signIn = findViewById(R.id.sign_in);
        mEmailEt = findViewById(R.id.emailEt);
        mPasswordEt = findViewById(R.id.passwordEt);
        mRegisterBtn = findViewById(R.id.registerBtn);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Next step...");

        mRegisterBtn.setOnClickListener(v -> {
            String email = mEmailEt.getText().toString().trim();
            String password = mPasswordEt.getText().toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                mEmailEt.setError("Invalid Email");
                mEmailEt.setFocusable(true);
            }else if (password.length() < 8){
                mPasswordEt.setError("Password length at least 8 chars");
                mPasswordEt.setFocusable(true);
            }else {
                RegisterUser(email, password);
            }
        });


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    private void RegisterUser(String email, String password) {

        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
//                        progressDialog.dismiss();
                        FirebaseUser user = mAuth.getCurrentUser();

                        String email1 = user.getEmail();
                        String uid = user.getUid();

                        HashMap<Object, String> hashMap = new HashMap<>();

                        hashMap.put("Email", email1);
                        hashMap.put("Uid", uid);
                        hashMap.put("ForExample", "You didn't add any personal information to transfers. Add them to get the correct payments.");
                        hashMap.put("First Name", "");
                        hashMap.put("Last Name", "");
                        hashMap.put("Gender", "");
                        hashMap.put("Country", "");
                        hashMap.put("Birthday", "");
                        hashMap.put("Street", "");
                        hashMap.put("House number", "");
                        hashMap.put("postal code", "");
                        hashMap.put("City", "");
                        hashMap.put("Street of region", "");
                        hashMap.put("Phone number (optional)", "");
                        hashMap.put("EarnMoney", "0.00");

                        FirebaseDatabase database = FirebaseDatabase.getInstance();

                        DatabaseReference reference = database.getReference("Users");

                        reference.child(uid).setValue(hashMap);

                        Toast.makeText(this, "Registered..\n" + user.getEmail(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistrActivity.this, BioActivity.class));
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        progressDialog.dismiss();
                        Toast.makeText(RegistrActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }

                    // ...
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Snackbar.make(findViewById(R.id.layoutReg), "" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}