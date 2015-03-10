package com.whiteandc.capture;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.whiteandc.capture.data.MonumentList;
import com.whiteandc.capture.data.MonumentLoader;
import com.whiteandc.capture.fragments.camera.FragmentCamera;

public class MonumentsActivityCopy extends ActionBarActivity implements OnPageChangeListener {

    private Toolbar toolbar;
	private ViewPager mViewPager;
	
	private String currentMonumentId = null;
	public FragmentCamera fragmentCamera= new FragmentCamera();


	private int currentImage = 0;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monuments);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.getMenu().clear();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
		//mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);

		// Set up the ViewPager with the sections adapter.
//		mViewPager = (ViewPager) findViewById(R.id.pager);
//		mViewPager.setAdapter(mSectionsPagerAdapter);
//		//TODO chanchullo arreglar
//        mViewPager.setOnPageChangeListener(mSectionsPagerAdapter.fragmentMonument);
//        //mViewPager.setOnPageChangeListener(new PageListener(this));

        MonumentLoader.loadMonuments(getPreferences(MODE_PRIVATE));
		if(MonumentList.getList().size()>0){
			currentMonumentId = MonumentList.getList().get(0).getName();
		}

	}

    public void enableHomeButton(){
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void disableHomeButton(){
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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
            mViewPager.setCurrentItem(0);
        }

        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}

	@Override
	public void onPageSelected(int position) {
		Log.i(getClass().getSimpleName(), "Recuperando imagen con posicion: "+position);
		if (currentMonumentId != null){
			Log.i(getClass().getSimpleName(), "Monumento: "+ currentMonumentId);
			int[] photos = MonumentList.getMonument(currentMonumentId).getPhotos();
			if (photos.length > 0){
				Log.i(getClass().getSimpleName(), "Imagen con id: "+photos[position]);
				currentImage  = photos[position];
			} 
		}
	}

    public Toolbar getToolBar(){
        return toolbar;
    }

    public final int getCurrentImg() {
        return currentImage;
    }

    public String getCurrentMonumentId(){
        return currentMonumentId;
    }


    public void setCurrentMonumentId(String monumentId){
        currentMonumentId = monumentId;
    }

}
