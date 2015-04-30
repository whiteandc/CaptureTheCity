package com.whiteandc.capture;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.commonsware.cwac.camera.CameraFragment;
import com.whiteandc.capture.data.Monument;
import com.whiteandc.capture.data.MonumentList;
import com.whiteandc.capture.data.MonumentLoader;
import com.whiteandc.capture.fragments.BasicFragment;
import com.whiteandc.capture.fragments.camera.FragmentCamera;
import com.whiteandc.capture.fragments.list.FragmentCityList;
import com.whiteandc.capture.fragments.notcaptured.FragmentNotCaptured;

import java.util.logging.Logger;

public class MonumentsActivity extends ActionBarActivity{

    private static final String CLASS = "MonumentsActivity";

    private Fragment selectedFragment;
    private Toolbar toolbar;
    private String currentMonumentId = null;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monuments);

        toolbarCreation();

        if (selectedFragment == null) {
            switchToFragmentCityList();
        } else {
            if (selectedFragment instanceof FragmentCamera){
                setToolBarVisibility(false);
            }
            switchFragment(selectedFragment);
        }

        MonumentLoader.loadMonuments(getPreferences(MODE_PRIVATE));
		if(MonumentList.getList().size()>0){
			currentMonumentId = MonumentList.getList().get(0).getName();
		}

	}

    private void toolbarCreation() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.getMenu().clear();
        setSupportActionBar(toolbar);
    }

    public void setToolbarTitle(String cityName) {
        if(toolbar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            Assets.setFont1(toolbarTitle, this);
            toolbarTitle.setText(cityName);
        }
    }

    public void setToolBarVisibility(boolean visible){
        int visibility = (visible) ? View.VISIBLE : View.GONE;
        if(toolbar != null) {
            toolbar.setVisibility(visibility);
        }
    }

    public void setFullScreen(boolean fullScreen){
        if(fullScreen){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }

    public void setHomeButtonVisibility(boolean visibility){
        getSupportActionBar().setHomeButtonEnabled(visibility);
        getSupportActionBar().setDisplayHomeAsUpEnabled(visibility);
    }

    public void switchToFragmentCityList(){
        switchFragment(new FragmentCityList());
    }

    public void switchToFragmentNotCaptured() {
        switchFragment(new FragmentNotCaptured());
    }

    public void switchToFragmentCamera(int currentPicture) {
        switchFragment(FragmentCamera.newInstance(currentPicture));
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
            switchToFragmentCityList();
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
        if(selectedFragment != null) {
            if(getFragmentManager().getBackStackEntryCount() != 1) {
                getFragmentManager().popBackStack();
            }
        }else{
            super.onBackPressed();
        }
    }

    public void setSelectedFragment(Fragment selectedFragment) {
        this.selectedFragment = selectedFragment;
    }
}
