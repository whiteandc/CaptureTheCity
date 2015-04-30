package com.whiteandc.capture;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
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

    private Fragment selectedFragment;
    private Toolbar toolbar;
    private String currentMonumentId = null;
    private ViewPager pager;
    private ViewPagerAdapterTabs adapter;
    private SlidingTabLayout tabs;
    private FrameLayout fragPlaceholder;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monuments);

        toolbarCreation();


        fragPlaceholder = (FrameLayout) findViewById(R.id.fragment_placeholder);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        CharSequence[] titles= new CharSequence[]{getResources().getString(R.string.tab_monuments), getResources().getString(R.string.tab_map)};
        adapter = new ViewPagerAdapterTabs(getFragmentManager(),titles , titles.length, new FragmentCityList(), new FragmentMap());


        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

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

    }

    private void toolbarCreation() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.getMenu().clear();
        setSupportActionBar(toolbar);
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

    public void switchToListAdapter() {//TODO refactor
        adapter.deleteFragments();
        //This adapter will be used when a monument is selected
        adapter.setFragments(new FragmentCityList(), new FragmentMap());
        adapter.setAdapterList(true);
        adapter.setTitles(new CharSequence[]{getResources().getString(R.string.tab_monuments), getResources().getString(R.string.tab_map)});
        tabs.updateTextTitles();
        pager.setAdapter(adapter);
    }

    public void switchToDetailAdapter() { //TODO refactor
        adapter.deleteFragments();
        //This adapter will be used when a monument is selected
        adapter.setFragments(new FragmentNotCaptured(), new FragmentMapDetail());
        adapter.setAdapterList(false);
        adapter.setTitles(new CharSequence[]{getResources().getString(R.string.tab_monument), getResources().getString(R.string.tab_map)});
        tabs.updateTextTitles();
        pager.setAdapter(adapter);
    }

    public void switchToFragmentCamera(int currentPicture) {
        tabs.setVisibility(View.GONE);
        pager.setVisibility(View.GONE);
        switchFragment(FragmentCamera.newInstance(currentPicture));
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
        if (!adapter.isAdapterList() && (selectedFragment != null) &&(selectedFragment instanceof FragmentCamera)) {
            getFragmentManager().beginTransaction().remove(selectedFragment).commit();
            tabs.setVisibility(View.VISIBLE);
            pager.setVisibility(View.VISIBLE);
            fragPlaceholder.setVisibility(View.GONE);
            setToolBarVisibility(true);
            selectedFragment= null;
        } else if (!adapter.isAdapterList()) {
            switchToListAdapter();
        } else {
            super.onBackPressed();
        }
    }

    public void setSelectedFragment(Fragment selectedFragment) {
        this.selectedFragment = selectedFragment;
    }
}
