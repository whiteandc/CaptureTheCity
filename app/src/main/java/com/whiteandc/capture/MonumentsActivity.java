package com.whiteandc.capture;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.whiteandc.capture.data.MonumentLoader;
import com.whiteandc.capture.fragments.list.FragmentCityList;
import com.whiteandc.capture.fragments.map.FragmentMap;
import com.whiteandc.capture.tabs.SlidingTabLayout;
import com.whiteandc.capture.tabs.ViewPagerAdapterTabs;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MonumentsActivity extends ActionBarActivity {

    private static final String CLASS = "MonumentsActivity";
    private static final int CAMERA_REQUEST_CODE = 10000;

    @Bind(R.id.app_bar) Toolbar toolbar;
    private String currentMonumentId;
    @Bind(R.id.pager) ViewPager pager;
    private ViewPagerAdapterTabs adapter;
    @Bind(R.id.tabs) SlidingTabLayout tabs;
    private OnBackPressedListener backPressedListener;
    private FragmentCityList fragmentCityList;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monuments);
        ButterKnife.bind(this);
        toolbarCreation();

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        CharSequence[] titles = new CharSequence[]{getResources().getString(R.string.tab_monuments), getResources().getString(R.string.tab_map)};
        adapter = new ViewPagerAdapterTabs(getFragmentManager(), titles, titles.length);

        // Assigning ViewPager View and setting the adapter
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
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
        switchToListAdapter();
    }

    private void toolbarCreation() {
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

    public void switchToListAdapter() {
        fragmentCityList= new FragmentCityList();
        switchToAdapter(fragmentCityList, new FragmentMap());
    }


    private void switchToAdapter(Fragment leftFragment, Fragment rightFragment){
        Log.i(CLASS, "switchToAdapter: "+leftFragment.getClass().getSimpleName());
        if(backPressedListener instanceof Fragment) {
            getFragmentManager().beginTransaction().remove((Fragment) backPressedListener).commit();
        }
        tabs.setVisibility(View.VISIBLE);
        pager.setVisibility(View.VISIBLE);
        backPressedListener = adapter;
        adapter.deleteFragments();
        adapter.setFragments(leftFragment, rightFragment);
        adapter.setTitles(new CharSequence[]{getResources().getString(R.string.tab_monument), getResources().getString(R.string.tab_map)});
        tabs.updateTextTitles();
        pager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.monuments, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fragmentCityList.searchMonument(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fragmentCityList.searchMonument(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        backPressedListener.onBackPressed(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(CLASS, "result: " + requestCode + "  " + resultCode);
        if(requestCode == CAMERA_REQUEST_CODE){
            currentMonumentId = data.getStringExtra(Assets.MONUMENT_ID);
            if(resultCode == RESULT_OK){
                Log.i(CLASS, "ok!");
                showCustomWebView();
                SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                editor.putString(currentMonumentId, "true");
            }else if(resultCode == RESULT_CANCELED){
                Log.i(CLASS, "cancelled!");
            }
        }

    }

    private void showCustomWebView() {
        new MaterialDialog.Builder(this)
                .theme(Theme.LIGHT)
                .title(R.string.congratulations)
                .titleColorRes(R.color.primaryColor)
                .content(getString(R.string.captured_msg) + " " + currentMonumentId)
                .positiveText(R.string.ok)
                .show();
    }



    public interface OnBackPressedListener {
        public void onBackPressed(MonumentsActivity monumentsActivity);
    }
}
