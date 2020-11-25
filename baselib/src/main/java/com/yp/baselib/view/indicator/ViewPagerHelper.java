package com.yp.baselib.view.indicator;

import androidx.viewpager.widget.ViewPager;

/**
 * 简化和ViewPager绑定
 * Created by hackware on 2016/8/17.
 */

public class ViewPagerHelper {
    public static void bind(final YxdIndicator YxdIndicator, ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                YxdIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                YxdIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                YxdIndicator.onPageScrollStateChanged(state);
            }
        });
    }
}

