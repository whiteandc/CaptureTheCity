package com.whiteandc.capture;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.whiteandc.capture.data.MonumentList;
import com.whiteandc.capture.data.MonumentLoader;
import com.whiteandc.capture.fragments.camera.FragmentCamera;
import com.whiteandc.capture.fragments.list.FragmentCityList;
import com.whiteandc.capture.fragments.notcaptured.FragmentNotCaptured;

public class MonumentsActivity extends ActionBarActivity{

    private Toolbar toolbar;
    private String currentMonumentId = null;
	private int currentImage = 0;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monuments);

        toolbarCreation();

        switchToFragmentCityList();

        MonumentLoader.loadMonuments(getPreferences(MODE_PRIVATE));
		if(MonumentList.getList().size()>0){
			currentMonumentId = MonumentList.getList().get(0).getName();
		}

	}

    private void toolbarCreation() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.getMenu().clear();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setHomeButtonVisibility(boolean visibility){
        getSupportActionBar().setHomeButtonEnabled(visibility);
        getSupportActionBar().setDisplayHomeAsUpEnabled(visibility);
    }

    public void switchToFragmentCityList(){
        setHomeButtonVisibility(false);
        switchFragment(new FragmentCityList());
    }

    public void switchToFragmentNotCaptured() {
        setHomeButtonVisibility(true);
        switchFragment(new FragmentNotCaptured());
    }

    public void switchToFragmentCamera() {
        switchFragment(new FragmentCamera());
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, fragment);
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
}
