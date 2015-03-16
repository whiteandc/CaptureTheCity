package com.whiteandc.capture.fragments.list;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.whiteandc.capture.R;
import com.whiteandc.capture.data.Monument;
import com.whiteandc.capture.data.MonumentList;
import com.whiteandc.capture.fragments.BasicFragment;

import java.lang.reflect.Field;
import java.util.Hashtable;

public class FragmentCityList extends BasicFragment implements AdapterView.OnItemClickListener, ViewTreeObserver.OnGlobalLayoutListener {

	private CityListAdapter adapter = null;
    private ListView list;
    private ViewTreeObserver vto;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_city_list, container, false);

	    adapter = new CityListAdapter(this.getActivity());
	    list= (ListView) rootView.findViewById(R.id.city_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);

        vto = monumentActivity.setToolBarSizeObserver(this);

        monumentActivity.setToolbarTitle(MonumentList.getCityName());
		return rootView;
	}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Monument monument= MonumentList.getList().get(position-1);
        monumentActivity.setCurrentMonumentId(monument.getName());
        monumentActivity.switchToFragmentNotCaptured();
    }

    @Override
    public void onGlobalLayout() {
        int height = monumentActivity.getToolbarHeight();

        View padding = new View(getActivity());
        padding.setLayoutParams(new AbsListView.LayoutParams(0, height));

        list.addHeaderView(padding);
        list.setSelection(0);

        try { // Using reflection to access to private fields maybe not the best way to do this
            Field f = vto.getClass().getDeclaredField("mAlive");
            f.setAccessible(true);
            f.set(vto, true);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(),"", e);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            vto.removeOnGlobalLayoutListener(this);
        } else {
            vto.removeGlobalOnLayoutListener(this);
        }


    }
}
