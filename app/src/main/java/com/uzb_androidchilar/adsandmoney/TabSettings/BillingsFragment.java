package com.uzb_androidchilar.adsandmoney.TabSettings;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
import com.uzb_androidchilar.adsandmoney.R;

import java.util.HashMap;

public class BillingsFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Query query;
    private ProgressDialog progressDialog;
    private Button saveData;

    private EditText mFirstName, mLastName, mStreet, mHouseNumber, mPostalCode, mCity, mRegion;

    private String forFirstName, forLastName, forStreet, forHouseNumber, forPostalCode, forCity, forRegion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_billings, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        mFirstName = view.findViewById(R.id.firstNameEt);
        mLastName = view.findViewById(R.id.lastNameEt);
        mStreet = view.findViewById(R.id.streetEt);
        mHouseNumber = view.findViewById(R.id.houseNumberEt);
        mPostalCode = view.findViewById(R.id.postalCodeEt);
        mCity = view.findViewById(R.id.cityEt);
        mRegion = view.findViewById(R.id.stateOrRegionEt);

        saveData = view.findViewById(R.id.saveData);


        query = databaseReference.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    forFirstName = "" + ds.child("First Name").getValue();
                    forLastName = "" + ds.child("Last Name").getValue();
                    forStreet = "" + ds.child("Street").getValue();
                    forHouseNumber = "" + ds.child("House number").getValue();
                    forPostalCode = "" + ds.child("postal code").getValue();
                    forCity = "" + ds.child("City").getValue();
                    forRegion = "" + ds.child("Street of region").getValue();

                    mFirstName.setText(forFirstName);
                    mLastName.setText(forLastName);
                    mStreet.setText(forStreet);
                    mHouseNumber.setText(forHouseNumber);
                    mPostalCode.setText(forPostalCode);
                    mCity.setText(forCity);
                    mRegion.setText(forRegion);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mFirstName.addTextChangedListener(ownDataSave);
        mLastName.addTextChangedListener(ownDataSave);
        mStreet.addTextChangedListener(ownDataSave);
        mHouseNumber.addTextChangedListener(ownDataSave);
        mPostalCode.addTextChangedListener(ownDataSave);
        mCity.addTextChangedListener(ownDataSave);
        mRegion.addTextChangedListener(ownDataSave);

        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Your data is storing in the database");

                String firstName = mFirstName.getText().toString().trim();
                String lastName = mLastName.getText().toString().trim();
                String street = mStreet.getText().toString().trim();
                String houseNumber = mHouseNumber.getText().toString().trim();
                String postalCode = mPostalCode.getText().toString().trim();
                String city = mCity.getText().toString().trim();
                String region = mRegion.getText().toString().trim();

                if (firstName.length() < 2 || Patterns.PHONE.matcher(firstName).matches()){
                    mFirstName.setError("Enter your full name");
                    mFirstName.setFocusable(true);
                } else if (lastName.length() < 2 || Patterns.PHONE.matcher(firstName).matches()) {
                    mLastName.setError("Enter your full name");
                    mLastName.setFocusable(true);
                } else if (street.isEmpty()) {
                    mStreet.setError("Enter your street");
                    mStreet.setFocusable(true);
                } else if (houseNumber.isEmpty()) {
                    mHouseNumber.setError("Enter your house number");
                    mHouseNumber.setFocusable(true);
                } else if (postalCode.isEmpty()) {
                    mPostalCode.setError("Enter your zip code");
                    mPostalCode.setFocusable(true);
                } else if (city.isEmpty()) {
                    mCity.setError("Enter your city");
                    mCity.setFocusable(true);
                } else if (region.isEmpty()) {
                    mRegion.setError("Enter your state or region");
                    mCity.setFocusable(true);
                }else {
                    progressDialog.show();

                    HashMap<String, Object> updateData = new HashMap<>();
                    updateData.put("First Name", firstName);
                    updateData.put("Last Name", lastName);
                    updateData.put("Street", street);
                    updateData.put("House number", houseNumber);
                    updateData.put("postal code", postalCode);
                    updateData.put("City", city);
                    updateData.put("Street of region", region);

                    databaseReference.child(user.getUid()).updateChildren(updateData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    Snackbar.make(view, "Your data has been successfully saved to the Database!", Snackbar.LENGTH_SHORT).show();

                                }
                            });

                }

            }
        });

        return view;
    }
    private TextWatcher ownDataSave = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String firstName = mFirstName.getText().toString().trim();
            String lastName = mLastName.getText().toString().trim();
            String street = mStreet.getText().toString().trim();
            String houseNumber = mHouseNumber.getText().toString().trim();
            String postalCode = mPostalCode.getText().toString().trim();
            String city = mCity.getText().toString().trim();
            String region = mRegion.getText().toString().trim();
            if ((firstName.length() >= 2 || Patterns.PHONE.matcher(firstName).matches()) &&
                    (lastName.length() >= 2 || Patterns.PHONE.matcher(firstName).matches()) &&
                    !street.isEmpty() && !houseNumber.isEmpty() && !postalCode.isEmpty() &&
                    !city.isEmpty() && !region.isEmpty()) {

                saveData.setEnabled(true);
                saveData.setBackgroundResource(R.drawable.earn_button_back);
                saveData.setTextColor(Color.WHITE);

            } else {
                saveData.setBackgroundColor(Color.rgb(214, 216, 215));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}