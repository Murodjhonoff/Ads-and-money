package com.uzb_androidchilar.adsandmoney;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltipUtils;


public class HomeFragment extends Fragment{

    SwipeRefreshLayout swipeRefreshLayout;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Query query;

    TextView todayEarn, lastSevenDay, thisMonth;
    String readBase;
    float add;

//    private ImageView post1, post2;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        todayEarn = view.findViewById(R.id.today_earn);
        lastSevenDay = view.findViewById(R.id.seven_earn);
        thisMonth = view.findViewById(R.id.this_month_earn);


//        post1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new SimpleTooltip.Builder(getContext())
//                        .anchorView(post1)
//                        .text("Simple Text for example")
//                        .gravity(Gravity.BOTTOM)
//                        .dismissOnInsideTouch(false)
//                        .dismissOnOutsideTouch(true)
//                        .animated(true)
//                        .showArrow(false)
//                        .animationPadding(30f)
//                        .transparentOverlay(true)
//                        .animationDuration(1000)
//                        .contentView(R.layout.corner_tooltip, R.id.text)
//                        .build()
//                        .show();
//            }
//        });
//        post2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new SimpleTooltip.Builder(getContext())
//                        .anchorView(post2)
//                        .text("Simple Text for example")
//                        .gravity(Gravity.BOTTOM)
//                        .animated(true)
//                        .dismissOnInsideTouch(false)
//                        .dismissOnOutsideTouch(true)
//                        .transparentOverlay(true)
//                        .showArrow(false)
//                        .animationPadding(30f)
//                        .build()
//                        .show();
//            }
//        });

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        query = databaseReference.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    readBase = "" + ds.child("EarnMoney").getValue();
                    add = Float.parseFloat(readBase);
                    todayEarn.setText("$" + add);
                    lastSevenDay.setText("$" + add);
                    thisMonth.setText("$" + add);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 4000);
            }
        });

        return view;

    }

    //onResume method in HomeFragment

    public void onResume() {
        super.onResume();
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    float earn1 = ds.child("Earn").getValue(Float.class);
//                    earn.add = earn1;
//                    todayEarn.setText("$" + earn.add);
//                    lastSevenDay.setText("$" + earn.add);
//                    thisMonth.setText("$" + earn.add);
//                }
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }


    //    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        for_result = view.findViewById(R.id.today_earn);
//        Bundle bundle = this.getArguments();
//        if (bundle != null){
//            SharedPreferences sharedPreferences  = context.getSharedPreferences("sharedPref", MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putFloat("float", f);
//            add = sharedPreferences.getFloat("float", f);
//            f = bundle.getFloat("float");
//            for_result.setText("$" + add);
//        }
//    }
}