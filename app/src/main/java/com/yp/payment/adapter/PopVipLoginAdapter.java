package com.yp.payment.adapter;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.yp.payment.fragment.CardVipLoginFragment;
import com.yp.payment.fragment.PhoneVipLoginFragment;
import com.yp.payment.fragment.ScanVipLoginFragment;

public class PopVipLoginAdapter extends FragmentPagerAdapter {
    PhoneVipLoginFragment phoneVipLoginFragment;
    CardVipLoginFragment cardVipLoginFragment;
    ScanVipLoginFragment scanVipLoginFragment;

    public PopVipLoginAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                if (phoneVipLoginFragment == null) {
                    phoneVipLoginFragment = new PhoneVipLoginFragment();
                }
                return phoneVipLoginFragment;
            case 1:
                if (cardVipLoginFragment == null) {
                    cardVipLoginFragment = new CardVipLoginFragment();
                }
                return cardVipLoginFragment;
            case 2:
                if (scanVipLoginFragment == null) {
                    scanVipLoginFragment = new ScanVipLoginFragment();
                }
                return scanVipLoginFragment;
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
