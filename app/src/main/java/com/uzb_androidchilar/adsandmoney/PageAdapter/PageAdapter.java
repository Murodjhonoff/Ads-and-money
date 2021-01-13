package com.uzb_androidchilar.adsandmoney.PageAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.uzb_androidchilar.adsandmoney.TabSettings.AboutFragment;
import com.uzb_androidchilar.adsandmoney.TabSettings.AccountFragment;
import com.uzb_androidchilar.adsandmoney.TabSettings.BillingsFragment;
import com.uzb_androidchilar.adsandmoney.TabSettings.PrivacyFragment;

public class PageAdapter extends FragmentStatePagerAdapter {
    private int countTab;

    public PageAdapter(FragmentManager fm, int countTab) {
        super(fm);
        this.countTab = countTab;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                AccountFragment accountFragment = new AccountFragment();
                return accountFragment;
            case 1:
                BillingsFragment billingsFragment = new BillingsFragment();
                return billingsFragment;
            case 2:
                PrivacyFragment privacyFragment = new PrivacyFragment();
                return privacyFragment;
            case 3:
                AboutFragment aboutFragment = new AboutFragment();
                return aboutFragment;
            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return countTab;
    }
}
