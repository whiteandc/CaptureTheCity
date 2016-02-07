package com.whiteandc.capture.fragments.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListView;

import com.whiteandc.capture.DetailActivity;
import com.whiteandc.capture.MonumentsActivity;
import com.whiteandc.capture.R;
import com.whiteandc.capture.data.Monument;
import com.whiteandc.capture.data.MonumentList;
import com.whiteandc.capture.fragments.BasicFragment;


public class FragmentCityList extends BasicFragment implements AdapterView.OnItemClickListener{

	private CityListAdapter adapter = null;
    private ListView list;
    private ViewTreeObserver vto;
    private View rootView;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
		rootView = inflater.inflate(R.layout.fragment_city_list, container, false);

        ((MonumentsActivity) mActivity).setFullScreen(false);
        ((MonumentsActivity) mActivity).setToolBarVisibility(true);
        ((MonumentsActivity) mActivity).setHomeButtonVisibility(false);

        adapter = new CityListAdapter(this.getActivity());
	    list= (ListView) rootView.findViewById(R.id.city_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);

        ((MonumentsActivity) mActivity).setToolbarTitle(MonumentList.getCityName());
		return rootView;
	}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Monument monument= MonumentList.getList().get(position);
        //((MonumentsActivity) mActivity).setCurrentMonumentId(monument.getName());
        switchToDetailActivity(monument.getName());
    }

    public void switchToDetailActivity(String monumentId) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
      //  View ivItem_img= adapter.getView();
     //   ActivityOptionsCompat options = ActivityOptionsCompat.
      //          makeSceneTransitionAnimation(getActivity(), ivItem_img, "img_trans");
        Bundle b= new Bundle();
        b.putString("monumentId", monumentId);
        intent.putExtras(b);
        startActivity(intent);
    }


    public void searchMonument(String query) {
        adapter.searchMonument(query);
    }
}
