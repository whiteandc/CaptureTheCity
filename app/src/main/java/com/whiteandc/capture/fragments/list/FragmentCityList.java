package com.whiteandc.capture.fragments.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.whiteandc.capture.R;
import com.whiteandc.capture.data.Monument;
import com.whiteandc.capture.data.MonumentList;
import com.whiteandc.capture.fragments.BasicFragment;

public class FragmentCityList extends BasicFragment implements AdapterView.OnItemClickListener {

	private CityListAdapter adapter = null;
    private ListView list;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_city_list, container, false);

	    adapter = new CityListAdapter(this.getActivity());
	    list= (ListView) rootView.findViewById(R.id.city_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
		
		return rootView;
	}


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Monument monument= MonumentList.getList().get(position);
        monumentActivity.setCurrentMonumentId(monument.getName());
        monumentActivity.switchToFragmentNotCaptured();
    }
}
