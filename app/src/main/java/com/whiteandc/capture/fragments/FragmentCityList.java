package com.whiteandc.capture.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ListView;

import com.whiteandc.capture.ListListener;
import com.whiteandc.capture.MonumentsActivity;
import com.whiteandc.capture.R;
import com.whiteandc.capture.adapters.CityListAdapter;

import java.lang.reflect.Field;

public class FragmentCityList extends Fragment implements ViewTreeObserver.OnGlobalLayoutListener{
	
	CityListAdapter adapter = null;
    private ListView list;
    private ViewTreeObserver vto;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_city_list, container, false);

	    adapter = new CityListAdapter(this.getActivity());
	    list= (ListView) rootView.findViewById(R.id.city_list);
        list.setAdapter(adapter);

        vto = ((MonumentsActivity) getActivity()).getToolBar().getViewTreeObserver();
        vto.addOnGlobalLayoutListener(this);
        ListListener listListener = new ListListener(rootView, (MonumentsActivity) getActivity());
        list.setOnItemClickListener(listListener);
		
		return rootView;
	}
	
	@Override
	public void onDetach() {
	    super.onDetach();

	    try {
	        Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
	        childFragmentManager.setAccessible(true);
	        childFragmentManager.set(this, null);
	    } catch (NoSuchFieldException e) {
	        throw new RuntimeException(e);
	    } catch (IllegalAccessException e) {
	        throw new RuntimeException(e);
	    }
	}

    @Override
    public void onGlobalLayout() {
        int height = ((MonumentsActivity) getActivity()).getToolBar().getHeight();

        View padding = new View(getActivity());
        padding.setLayoutParams(new AbsListView.LayoutParams(0, height));

        list.addHeaderView(padding);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            vto.removeOnGlobalLayoutListener(this);
        } else {
            vto.removeGlobalOnLayoutListener(this);
        }

    }
}
