package com.uzb_androidchilar.adsandmoney;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AdSize;
import com.facebook.ads.RewardedAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.UnityAds;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class EarnFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;

    private String gameId = "3904175";
    private String interstitialAdPlacement = "interstitial";
    private String rewardedVideoAdPlacement = "rewardedVideo";
    private String videoAdPlacement = "video";
    private boolean testMode = false;

    private static int adCount = 0;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Query query;

    private Button button, button2, button3, button4, button5;
    private TextView input;

    ProgressBar progress1, progress2, progress3, progress4, progress5;


    float add, for_add = (float) 0.01;
    String readBase;


    private static final String TAG = EarnFragment.class.getSimpleName();


    private final AdListener mAdListener = new AdListener(){
        @Override
        public void onAdClosed() {
            Log.d(TAG, "onAdClosed");
        }

        @Override
        public void onAdFailedToLoad(int loadAdError) {
            String message = "Unknown";
            switch (loadAdError){
                case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                    message = "ERROR_CODE_INTERNAL_ERROR";
                    break;
                case AdRequest.ERROR_CODE_INVALID_REQUEST:
                    message = "ERROR_CODE_INVALID_REQUEST";
                    break;
                case AdRequest.ERROR_CODE_NETWORK_ERROR:
                    message = "ERROR_CODE_NETWORK_ERROR";
                    break;
                case AdRequest.ERROR_CODE_NO_FILL:
                    message = "ERROR_CODE_NO_FILL";
                    break;
            }
            Log.d(TAG, message);
        }
        @Override
        public void onAdLeftApplication() {
            Log.d(TAG, "onAdLeftApplication");
        }
        @Override
        public void onAdOpened() {
            Log.d(TAG, "onAdOpened");
        }

        @Override
        public void onAdLoaded() {
            Log.d(TAG, "onAdLoaded");
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_earn, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        button = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);
        button3 = view.findViewById(R.id.button3);
        button4 = view.findViewById(R.id.button4);
        button5 = view.findViewById(R.id.button5);
        input = view.findViewById(R.id.input);

        button.setBackgroundResource(R.drawable.earn_button_back);
        button2.setBackgroundResource(R.drawable.earn_button_back);
        button3.setBackgroundResource(R.drawable.earn_button_back);
        button4.setBackgroundResource(R.drawable.earn_button_back);
        button5.setBackgroundResource(R.drawable.earn_button_back);

        progress1 = view.findViewById(R.id.progress1);
        progress2 = view.findViewById(R.id.progress2);
        progress3 = view.findViewById(R.id.progress3);
        progress4 = view.findViewById(R.id.progress4);
        progress5 = view.findViewById(R.id.progress5);


        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        query = databaseReference.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    readBase = "" + ds.child("EarnMoney").getValue();
                    add = Float.parseFloat(readBase);
                    input.setText("$" + add);
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
                }, 3000);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.GONE);
                button2.setVisibility(View.VISIBLE);
                button3.setVisibility(View.VISIBLE);
                button4.setVisibility(View.VISIBLE);
                button5.setVisibility(View.VISIBLE);

                progress1.setVisibility(View.VISIBLE);
                progress2.setVisibility(View.GONE);
                progress3.setVisibility(View.GONE);
                progress4.setVisibility(View.GONE);
                progress5.setVisibility(View.GONE);

                for_add = (float) ((add + 0.01) * 100);
                int result = (int) for_add;
                add = (float) (result/100.0);

                String money = String.valueOf(add);

                HashMap<String, Object> updateEarn = new HashMap<>();
                updateEarn.put("EarnMoney", money);

                databaseReference.child(user.getUid()).updateChildren(updateEarn)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progress1.setVisibility(View.GONE);
                                button.setVisibility(View.VISIBLE);
                            }
                        });

            }
        });


        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UnityAds.initialize(getContext(), gameId, false);
        IUnityAdsListener unityAdsListener = new IUnityAdsListener() {
            @Override
            public void onUnityAdsReady(String s) {
                progress2.setVisibility(View.GONE);
                progress3.setVisibility(View.GONE);
                progress4.setVisibility(View.GONE);
                progress5.setVisibility(View.GONE);
                button2.setVisibility(View.VISIBLE);
                button3.setVisibility(View.VISIBLE);
                button4.setVisibility(View.VISIBLE);
                button5.setVisibility(View.VISIBLE);

            }

            @Override
            public void onUnityAdsStart(String s) {
                Toast.makeText(getContext(), "Interstitial Ad playing", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {

                if (finishState.equals(UnityAds.FinishState.COMPLETED)) {
                    for_add = (float) ((add + 0.02) * 100);
                    int result = (int) for_add;
                    add = (float) (result/100.0);

                    adCount++;

                    String money = String.valueOf(add);

                    HashMap<String, Object> updateEarn = new HashMap<>();
                    updateEarn.put("EarnMoney", money);

                    databaseReference.child(user.getUid()).updateChildren(updateEarn)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });

                    Toast.makeText(getContext(), "Interstitial Ad finished", Toast.LENGTH_SHORT).show();
                } else if (finishState.equals(UnityAds.FinishState.SKIPPED)) {
                    Toast.makeText(getContext(), "Interstitial Ad skipped", Toast.LENGTH_SHORT).show();
                } else if (finishState.equals(UnityAds.FinishState.ERROR)) {
                    Toast.makeText(getContext(), "Interstitial Ad error", Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {
                Toast.makeText(getContext(), unityAdsError.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        UnityAds.setListener(unityAdsListener);
        UnityAds.addListener(unityAdsListener);


//        IUnityAdsLoadListener unityAdsLoadListener = new IUnityAdsLoadListener() {
//            @Override
//            public void onUnityAdsAdLoaded(String s) {
//                Toast.makeText(getContext(), "Interstitial Ad Loaded", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onUnityAdsFailedToLoad(String s) {
//
//            }
//        };

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button.setVisibility(View.VISIBLE);
                button2.setVisibility(View.GONE);
                button3.setVisibility(View.VISIBLE);
                button4.setVisibility(View.VISIBLE);
                button5.setVisibility(View.VISIBLE);

                progress1.setVisibility(View.GONE);
                progress2.setVisibility(View.VISIBLE);
                progress3.setVisibility(View.GONE);
                progress4.setVisibility(View.GONE);
                progress5.setVisibility(View.GONE);

                UnityAds.load(interstitialAdPlacement);
                displayInterstitialAd();

            }
            private void displayInterstitialAd(){
                if (UnityAds.isReady(interstitialAdPlacement)){
                    UnityAds.show(getActivity(), interstitialAdPlacement);
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button.setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);
                button3.setVisibility(View.GONE);
                button4.setVisibility(View.VISIBLE);
                button5.setVisibility(View.VISIBLE);

                progress1.setVisibility(View.GONE);
                progress2.setVisibility(View.GONE);
                progress3.setVisibility(View.VISIBLE);
                progress4.setVisibility(View.GONE);
                progress5.setVisibility(View.GONE);

                UnityAds.load(interstitialAdPlacement);
                displayInterstitialAd();

            }
            private void displayInterstitialAd(){
                if (UnityAds.isReady(interstitialAdPlacement)){
                    UnityAds.show(getActivity(), interstitialAdPlacement);
                }
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);
                button3.setVisibility(View.VISIBLE);
                button4.setVisibility(View.GONE);
                button5.setVisibility(View.VISIBLE);

                progress1.setVisibility(View.GONE);
                progress2.setVisibility(View.GONE);
                progress3.setVisibility(View.GONE);
                progress4.setVisibility(View.VISIBLE);
                progress5.setVisibility(View.GONE);
                displayRewardedAd ();
            }
            private void displayRewardedAd () {
                if (UnityAds.isReady (rewardedVideoAdPlacement)) {
                    button4.setEnabled (false);
                    UnityAds.show (getActivity(), rewardedVideoAdPlacement);
                }
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);
                button3.setVisibility(View.VISIBLE);
                button4.setVisibility(View.VISIBLE);
                button5.setVisibility(View.GONE);

                progress1.setVisibility(View.GONE);
                progress2.setVisibility(View.GONE);
                progress3.setVisibility(View.GONE);
                progress4.setVisibility(View.GONE);
                progress5.setVisibility(View.VISIBLE);
                UnityAds.load(videoAdPlacement);
                displayVideoAd();
            }
            private void displayVideoAd () {
                if (UnityAds.isReady (videoAdPlacement)) {
                    UnityAds.show (getActivity(), videoAdPlacement);
                }
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();

        if (adCount == 6){
            button.setEnabled(false);
            button2.setEnabled(false);
            button3.setEnabled(false);
            button4.setEnabled(false);
            button5.setEnabled(false);
        }

//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    add = snapshot.getValue(Float.class);
//                    input.setText("$" + add);
//                }
////                for (DataSnapshot ds: snapshot.getChildren()){
////                    add = ds.child("Earn").getValue(Float.class);
////                    input.setText("$" + add);
////                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        AdView mAdMobView = (AdView) getView().findViewById(R.id.adView);

        mAdMobView.setAdListener(mAdListener);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdMobView.loadAd(adRequest);
    }





}