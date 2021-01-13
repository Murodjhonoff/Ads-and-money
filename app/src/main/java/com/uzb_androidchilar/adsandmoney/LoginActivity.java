package com.uzb_androidchilar.adsandmoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private TextView signUp, forgotText;
    private EditText mEmailEt, mPasswordEt;
    private Button mLoginBtn;

    ProgressDialog progressDialog;
    FirebaseUser user;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUp = findViewById(R.id.sign_up);
        mEmailEt = findViewById(R.id.emailEt);
        mPasswordEt = findViewById(R.id.passwordEt);
        mLoginBtn = findViewById(R.id.loginBtn);
//        forgotText = findViewById(R.id.forgot_password);

        mAuth = FirebaseAuth.getInstance();

        mLoginBtn.setOnClickListener(v -> {
            String email = mEmailEt.getText().toString().trim();
            String password = mPasswordEt.getText().toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                mEmailEt.setError("Invalid Email");
                mEmailEt.setFocusable(true);
            }else {
                loginUser(email, password);
            }
        });

        signUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegistrActivity.class));
            finish();
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");

    }

    private void loginUser(String email, String password) {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

//                            if (task.getResult().getAdditionalUserInfo().isNewUser()) {
//
//                                String uid = user.getUid();
//
//                                HashMap<Object, Float> hashMap = new HashMap<>();
//
//                                hashMap.put("Earn", (float) 0.00);
//
//                                FirebaseDatabase database = FirebaseDatabase.getInstance();
//
//                                DatabaseReference reference = database.getReference("Users");
//
//                                reference.child(uid).setValue(hashMap);
//                            }

                            startActivity(new Intent(LoginActivity.this, DescriptionActivity.class));
                            finish();
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Snackbar.make(findViewById(R.id.layoutLog), "" + e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return super.onNavigateUp();
    }
}