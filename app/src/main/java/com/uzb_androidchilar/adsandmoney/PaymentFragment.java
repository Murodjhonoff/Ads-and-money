package com.uzb_androidchilar.adsandmoney;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PaymentFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Query query;
    private ProgressDialog progressDialog;

    RelativeLayout dataCancel;
    TextView dataSave, forUpdateOwnData,forUpdateOwnData1, forUpdateOwnData2, forUpdateOwnData3;

    private EditText mFirstName, mLastName, mStreet, mHouseNumber, mPostalCode, mCity, mRegion;

    private String readBase, forExample, forFirstName, forLastName, forStreet, forHouseNumber, forPostalCode, forCity, forRegion;
    float add;
    private TextView earnReached, forPercentage,viewTransactions, add_payment, addFilOwnData;

    PieChart earnMoneyChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        earnMoneyChart = view.findViewById(R.id.statisticEarn);
        earnReached = view.findViewById(R.id.earnReached);
        forPercentage = view.findViewById(R.id.reachedForPayment);

        viewTransactions = view.findViewById(R.id.viewTransactions);
        add_payment = view.findViewById(R.id.add_payment);
        forUpdateOwnData1 = view.findViewById(R.id.updateOwndata1);
        addFilOwnData = view.findViewById(R.id.add_ownFillData);
        forUpdateOwnData = view.findViewById(R.id.updateOwndata);
        forUpdateOwnData2 = view.findViewById(R.id.updateOwndata2);
        forUpdateOwnData3 = view.findViewById(R.id.updateOwndata3);

        earnMoneyChart.setUsePercentValues(true);
        earnMoneyChart.getDescription().setEnabled(false);
        earnMoneyChart.setExtraOffsets(5, 10, 5, 5);
        earnMoneyChart.setDragDecelerationFrictionCoef(0.95f);

        earnMoneyChart.setDrawHoleEnabled(true);
        earnMoneyChart.setHoleColor(Color.WHITE);
        earnMoneyChart.setTransparentCircleRadius(61f);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        ArrayList<PieEntry> yValues = new ArrayList<>();

        query = databaseReference.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    readBase = "" + ds.child("EarnMoney").getValue();
                    add = Float.parseFloat(readBase);

                    yValues.add(new PieEntry(add, "Earn"));
                    yValues.add(new PieEntry(100 - add, "The rest"));

                    earnMoneyChart.animateY(1500, Easing.EaseInOutCubic);

                    PieDataSet dataSet = new PieDataSet(yValues, ":  Money");
                    dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(5f);
                    dataSet.setColors(Color.rgb(0, 44, 133), Color.rgb(20, 100, 249));

                    PieData data = new PieData(dataSet);
                    data.setValueTextColor(Color.YELLOW);
                    data.setValueTextSize(10f);

                    earnMoneyChart.setData(data);

                    if (add < 100) {
                        forPercentage.setText("You've reached " + add + "% of payment threshold");
                    } else if (add >= 100) {
                        earnReached.setText("You have reached the payment limit!");
                        forPercentage.setText("You've reached 100% of payment threshold");

                        viewTransactions.setEnabled(true);
                        add_payment.setEnabled(true);
                        viewTransactions.setTextColor(Color.rgb(0, 0, 21));
                        add_payment.setTextColor(Color.rgb(0, 0, 21));

                        viewTransactions.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), "This is view Transactions", Toast.LENGTH_SHORT).show();
                            }
                        });
                        add_payment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), "This is add Payment view", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addFilOwnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog fillOwnData = new Dialog(getContext());
                fillOwnData.setContentView(R.layout.fill_own_data);
                fillOwnData.setCancelable(false);
                fillOwnData.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);
                fillOwnData.getWindow().getAttributes().windowAnimations =
                        android.R.style.Animation_Toast;


                fillOwnData.show();

                mFirstName = fillOwnData.findViewById(R.id.firstNameEt);
                mLastName = fillOwnData.findViewById(R.id.lastNameEt);
                mStreet = fillOwnData.findViewById(R.id.streetEt);
                mHouseNumber = fillOwnData.findViewById(R.id.houseNumberEt);
                mPostalCode = fillOwnData.findViewById(R.id.postalCodeEt);
                mCity = fillOwnData.findViewById(R.id.cityEt);
                mRegion = fillOwnData.findViewById(R.id.stateOrRegionEt);

                dataCancel = fillOwnData.findViewById(R.id.ownDataCancel);
                dataSave = fillOwnData.findViewById(R.id.ownDataSave);

                dataCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fillOwnData.cancel();
                    }
                });

                mFirstName.addTextChangedListener(ownDataSave);
                mLastName.addTextChangedListener(ownDataSave);
                mStreet.addTextChangedListener(ownDataSave);
                mHouseNumber.addTextChangedListener(ownDataSave);
                mPostalCode.addTextChangedListener(ownDataSave);
                mCity.addTextChangedListener(ownDataSave);
                mRegion.addTextChangedListener(ownDataSave);

                dataSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("Your data has been successfully saved to the Database!");

                        String forExample = " ";
                        String firstName = mFirstName.getText().toString().trim();
                        String lastName = mLastName.getText().toString().trim();
                        String street = mStreet.getText().toString().trim();
                        String houseNumber = mHouseNumber.getText().toString().trim();
                        String postalCode = mPostalCode.getText().toString().trim();
                        String city = mCity.getText().toString().trim();
                        String region = mRegion.getText().toString().trim();

                        if (firstName.length() < 2 || Patterns.PHONE.matcher(firstName).matches()) {
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
                        } else {
                            progressDialog.show();

                            HashMap<String, Object> updateOwnData = new HashMap<>();
                            updateOwnData.put("ForExample", forExample);
                            updateOwnData.put("First Name", firstName);
                            updateOwnData.put("Last Name", lastName);
                            updateOwnData.put("Street", street);
                            updateOwnData.put("House number", houseNumber);
                            updateOwnData.put("postal code", postalCode);
                            updateOwnData.put("City", city);
                            updateOwnData.put("Street of region", region);

                            databaseReference.child(user.getUid()).updateChildren(updateOwnData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            fillOwnData.dismiss();
                                            updatedata();
                                        }
                                    });

                        }

                    }
                });

            }
            private void updatedata(){
                getFragmentManager().beginTransaction().replace(R.id.container, new PaymentFragment()).commit();
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

                        dataSave.setEnabled(true);
                        dataSave.setBackgroundResource(R.drawable.for_dialog_data_button);
                        dataSave.setTextColor(Color.WHITE);

                    } else {
                        dataSave.setEnabled(false);
                        dataSave.setBackgroundResource(R.color.white);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };


        });

        query = databaseReference.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    forExample = "" + ds.child("ForExample").getValue();
                    forFirstName = "" + ds.child("First Name").getValue();
                    forLastName = "" + ds.child("Last Name").getValue();
                    forStreet = "" + ds.child("Street").getValue();
                    forHouseNumber = "" + ds.child("House number").getValue();
                    forPostalCode = "" + ds.child("postal code").getValue();
                    forCity = "" + ds.child("City").getValue();
                    forRegion = "" + ds.child("Street of region").getValue();

                    forUpdateOwnData1.setText(forExample);
                    forUpdateOwnData.setText(forFirstName + " " + forLastName);
                    forUpdateOwnData2.setText(forStreet + " " + forHouseNumber + ", " + forPostalCode);
                    forUpdateOwnData3.setText(forCity + " " + forRegion);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}