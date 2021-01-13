package com.uzb_androidchilar.adsandmoney.TabSettings;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uzb_androidchilar.adsandmoney.CountryDetails;
import com.uzb_androidchilar.adsandmoney.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PrivacyFragment extends Fragment {
//    private Spinner spinner;
    private CountryDetails details = new CountryDetails();
    private TextView textView;
    private Button button;
    private EditText mFirstName, mLastName, mPhoneNumber;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Query query;

    private String forFirstName, forLastName, forPhoneNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_privacy, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

//        spinner = view.findViewById(R.id.spinner);
        textView = view.findViewById(R.id.simpleItem);
        mFirstName = view.findViewById(R.id.firstNameEt);
        mLastName = view.findViewById(R.id.lastNameEt);
        mPhoneNumber = view.findViewById(R.id.phoneNumberEt);
        button = view.findViewById(R.id.saveData);
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, details.getCountry());
//
//        spinner.setAdapter(adapter);

        query = databaseReference.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    forFirstName = "" + ds.child("First Name").getValue();
                    forLastName = "" + ds.child("Last Name").getValue();
                    forPhoneNumber = "" + ds.child("Phone number (optional)").getValue();

                    mFirstName.setText(forFirstName);
                    mLastName.setText(forLastName);
                    mPhoneNumber.setText(forPhoneNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String addGender = String.valueOf(spinner.getSelectedItem());
                String firstName = mFirstName.getText().toString().trim();
                String lastName = mLastName.getText().toString().trim();
                String phoneNumber = mPhoneNumber.getText().toString().trim();

                if (firstName.length() < 2 || Patterns.PHONE.matcher(firstName).matches()) {
                    mFirstName.setError("Enter your full name");
                    mFirstName.setFocusable(true);
                } else if (lastName.length() < 2 || Patterns.PHONE.matcher(firstName).matches()) {
                    mLastName.setError("Enter your full name");
                    mLastName.setFocusable(true);
                }else if (phoneNumber.length() < 5) {
                    mPhoneNumber.setError("Enter your phone number");
                    mPhoneNumber.setFocusable(true);
                }else {
                    HashMap<String, Object> saveOwnData = new HashMap<>();
//                    saveOwnData.put("Gender", addGender);
                    saveOwnData.put("First Name", firstName);
                    saveOwnData.put("Last Name", lastName);
                    saveOwnData.put("Phone number (optional)", phoneNumber);

                    databaseReference.child(user.getUid()).updateChildren(saveOwnData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Snackbar snackbar = Snackbar.make(view, "Your data is stored in the database", Snackbar.LENGTH_SHORT);
                                    snackbar.setBackgroundTint(Color.rgb(17,218,3));
                                    snackbar.setDuration(3000);
                                    snackbar.show();
                                }
                            });

                }
//                String[] a1 = {"A", "B", "C"};
//                String[] a2 = {"1", "2", "3"};
//                ArrayList<StringBuilder> results = new ArrayList();
//                for (int i = 0; i < a1.length; i++){
//                    String index = a1[i];
//                    String value = a2[i];
//                    StringBuilder builder = new StringBuilder();
//                    builder.append(index);
//                    builder.append(value);
//
//                    results.add(builder);
//                }
//                System.out.print(results);
            }
        });

        return view;
    }
//    public static void count(String s){
//        char[] ch = s.toCharArray();
//        int letter = 0;
//        for (int i = 0; i < s.length(); i++) {
//            if (Character.isLetter(ch[i])) letter++;
//        }
//        System.out.print(letter);
//    }

}