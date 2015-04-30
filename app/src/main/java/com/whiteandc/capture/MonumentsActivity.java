package com.whiteandc.capture;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.whiteandc.capture.data.MonumentList;
import com.whiteandc.capture.data.MonumentLoader;
import com.whiteandc.capture.fragments.FragmentMap;
import com.whiteandc.capture.fragments.FragmentMapDetail;
import com.whiteandc.capture.fragments.camera.FragmentCamera;
import com.whiteandc.capture.fragments.list.FragmentCityList;
import com.whiteandc.capture.fragments.notcaptured.FragmentNotCaptured;
import com.whiteandc.capture.tabs.SlidingTabLayout;
import com.whiteandc.capture.tabs.ViewPagerAdapterTabs;

public class MonumentsActivity extends ActionBarActivity {

    private static final String CLASS = "MonumentsActivity";
    public static final String CAMERA = "CAMERA";

    private Toolbar toolbar;
    private String currentMonumentId;
    private ViewPager pager;
    private ViewPagerAdapterTabs adapter;
    private SlidingTabLayout tabs;
    private FrameLayout fragPlaceholder;
    private OnBackPressedListener backPressedListener;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monuments);
        toolbarCreation();

        fragPlaceholder = (FrameLayout) findViewById(R.id.fragment_placeholder);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        CharSequence[] titles = new CharSequence[]{getResources().getString(R.string.tab_monuments), getResources().getString(R.string.tab_map)};
        adapter = new ViewPagerAdapterTabs(getFragmentManager(), titles, titles.length);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // Makes the Tabs Fixed

        // TODO Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.accentColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        MonumentLoader.loadMonuments(getPreferences(MODE_PRIVATE));

        if (MonumentList.getList().size() > 0) {
            currentMonumentId = MonumentList.getList().get(0).getName();
        }

        if(savedInstanceState != null && savedInstanceState.getBoolean(CAMERA)){
            switchToFragmentCamera(MonumentList.getMonument(currentMonumentId).getPhotos()[0]);
        }else {
            switchToListAdapter();
        }
    }

    private void toolbarCreation() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.getMenu().clear();
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        // super.onSaveInstanceState(outState);
        outState.putBoolean(CAMERA, true);
    }

    public void setToolbarTitle(String cityName) {
        if (toolbar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            Assets.setFont1(toolbarTitle, this);
            toolbarTitle.setText(cityName);
        }
    }

    public void setToolBarVisibility(boolean visible) {
        int visibility = (visible) ? View.VISIBLE : View.GONE;
        if (toolbar != null) {
            toolbar.setVisibility(visibility);
        }
    }

    public void setFullScreen(boolean fullScreen) {
        if (fullScreen) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }

    public void setHomeButtonVisibility(boolean visibility) {
        getSupportActionBar().setHomeButtonEnabled(visibility);
        getSupportActionBar().setDisplayHomeAsUpEnabled(visibility);
    }

    public void switchToListAdapter() {
        switchToAdapter(new FragmentCityList(), new FragmentMap());
    }

    public void switchToDetailAdapter() {
        switchToAdapter(new FragmentNotCaptured(), new FragmentMapDetail());
    }

    private void switchToAdapter(Fragment leftFragment, Fragment rightFragment){
        Log.i(CLASS, "switchToAdapter: "+leftFragment.getClass().getSimpleName());
        if(backPressedListener instanceof Fragment) {
            getFragmentManager().beginTransaction().remove((Fragment) backPressedListener).commit();
        }
        tabs.setVisibility(View.VISIBLE);
        pager.setVisibility(View.VISIBLE);
        fragPlaceholder.setVisibility(View.GONE);
        backPressedListener = adapter;
        adapter.deleteFragments();
        adapter.setFragments(leftFragment, rightFragment);
        adapter.setTitles(new CharSequence[]{getResources().getString(R.string.tab_monument), getResources().getString(R.string.tab_map)});
        tabs.updateTextTitles();
        pager.setAdapter(adapter);
    }

    public void switchToFragmentCamera(int currentPicture) {
        final FragmentCamera fragmentCamera = FragmentCamera.newInstance(currentPicture);
        backPressedListener = fragmentCamera;
        tabs.setVisibility(View.GONE);
        pager.setVisibility(View.GONE);
        switchFragment(fragmentCamera);
        fragPlaceholder.setVisibility(View.VISIBLE);
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, fragment);
        ft.addToBackStack("back");
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.monuments, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            switchToListAdapter();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setCurrentMonumentId(String monumentId) {
        currentMonumentId = monumentId;
    }

    public String getCurrentMonumentId() {
        return currentMonumentId;
    }

    @Override
    public void onBackPressed() {
        backPressedListener.onBackPressed(this);
    }

    public interface OnBackPressedListener {
        public void onBackPressed(MonumentsActivity monumentsActivity);
    }
}
