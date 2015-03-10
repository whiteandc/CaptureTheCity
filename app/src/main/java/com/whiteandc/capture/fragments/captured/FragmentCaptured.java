package com.whiteandc.capture.fragments.captured;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.whiteandc.capture.R;

public class FragmentCaptured extends Fragment {
		private static final String CLASS = "FragmentDescription";
		private View rootView;
		

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			rootView = inflater.inflate(R.layout.fragment_monument_captured, container, false);

			loadCurrentMonumentData();
	    
			return rootView;
		}

		public void loadCurrentMonumentData() {
			if (rootView != null){
//				((MonumentsActivity) getParentFragment().getActivity()).onPageSelected(0);
//
//				Monument monument= MonumentList.getMonument(((MonumentsActivity) getParentFragment().getActivity()).getCurrentMonumentId());

				//TextView textView = (TextView) rootView.findViewById(R.id.monument_name);
				//textView.setText(monument.getName());
				
				ImageView imgView= (ImageView) rootView.findViewById(R.id.capturedImage);
				
				
			    String path = Environment.getExternalStoragePublicDirectory(
		                Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/CaptureTheCity/";
			    Log.i(CLASS, path);
//			    Drawable imgDrawable= Drawable.createFromPath(new File(path,monument.getCapturedImg()).getAbsolutePath());
//			    if(imgDrawable!=null){
//			    	imgView.setImageDrawable(imgDrawable);
//			    }else{
//			    	imgView.setImageResource(monument.getPhotos()[0]);
//			    }
//
//
//				TextView descriptionView = (TextView) rootView.findViewById(R.id.monument_descroption);
//				descriptionView.setText(monument.getDescription());
			}
		}

		@Override
		public void onDetach() {

		}

	}