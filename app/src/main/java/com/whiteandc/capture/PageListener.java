package com.whiteandc.capture;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.whiteandc.capture.adapters.SectionsPagerAdapter;

public class PageListener implements ViewPager.OnPageChangeListener {

    private MonumentsActivity monumentsActivity;

    public PageListener(MonumentsActivity monumentsActivity){
        this.monumentsActivity = monumentsActivity;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int page) {
        if (SectionsPagerAdapter.MONUMENT == page){
            monumentsActivity.enableHomeButton();

            Toolbar toolbar = monumentsActivity.getToolBar();
            toolbar.getBackground().setAlpha(0);
        }else if(SectionsPagerAdapter.CITY_LIST == page){
            monumentsActivity.disableHomeButton();
            Toolbar toolbar = monumentsActivity.getToolBar();
            toolbar.setBackgroundColor(monumentsActivity.getResources().getColor(R.color.primaryColor));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
}
