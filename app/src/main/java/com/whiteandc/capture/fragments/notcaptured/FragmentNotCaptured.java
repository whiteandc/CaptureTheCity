package com.whiteandc.capture.fragments.notcaptured;

import android.os.Bundle;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.CirclePageIndicator;
import com.whiteandc.capture.DetailActivity;
import com.whiteandc.capture.R;
import com.whiteandc.capture.fragments.BasicFragment;
import com.whiteandc.capture.data.Monument;
import com.whiteandc.capture.data.MonumentList;


public class FragmentNotCaptured extends BasicFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private static final String CLASS = "FragmentNotCaptured";
    private Monument monument;
    private ViewPagerAdapter adapter;
    private int currentPicture = 0;
    private DetailActivity mDetailActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //Log.i(CLASS, "monumentActivity.getCurrentMonumentId(): "+monumentActivity.getCurrentMonumentId());
        mDetailActivity= (DetailActivity) mActivity;
        // This is a fail safe method. It shouldn't be necessary
        if(mDetailActivity.getCurrentMonumentId() == null){
            mDetailActivity.setCurrentMonumentId(MonumentList.getList().get(0).getName());
          //TODO  mDetailActivity.switchToListAdapter();
            Log.w(CLASS, "Empty current monumentId, this may be due to an error during start activity for result");
        }
        monument = MonumentList.getMonument(mDetailActivity.getCurrentMonumentId());
        View rootView = inflater.inflate(R.layout.fragment_monument_not_captured, container, false);

        mDetailActivity.setFullScreen(false);
        mDetailActivity.setToolBarVisibility(true);
        mDetailActivity.setHomeButtonVisibility(true);

        adapter = new ViewPagerAdapter(mDetailActivity, monument.getPhotos());
        adapter.notifyDataSetChanged();
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.photo_pager);
        viewPager.setAdapter(adapter);

        //Bind the title indicator to the adapter
        CirclePageIndicator circleIndicator = (CirclePageIndicator)rootView.findViewById(R.id.indicator);
        circleIndicator.setViewPager(viewPager);
        circleIndicator.setOnPageChangeListener(this);
        rootView.findViewById(R.id.camera_fab).setOnClickListener(this);

        mDetailActivity.setToolbarTitle(monument.getName());

        return rootView;
    }

    @Override
    public void onClick(View v) {
        mDetailActivity.startCameraActivity(currentPicture);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        currentPicture = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
