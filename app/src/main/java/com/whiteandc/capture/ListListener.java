package com.whiteandc.capture;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.whiteandc.capture.data.Monument;
import com.whiteandc.capture.data.MonumentList;

public class ListListener implements OnItemClickListener{

	private View rootView = null;
	private MonumentsActivity activity = null;
	
	public ListListener(View rootView, MonumentsActivity activity) {
		this.rootView = rootView;
		this.activity = activity;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
		Monument monument= MonumentList.getList().get(position);
		activity.setCurrentMonumentId(monument.getName());
        Log.i(this.getClass().getSimpleName(), monument.getName() + " --> " + position);
		activity.changeToFragmentMonument();
	}
}
