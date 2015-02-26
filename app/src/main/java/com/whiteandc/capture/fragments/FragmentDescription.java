package com.whiteandc.capture.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whiteandc.capture.MonumentsActivity;
import com.whiteandc.capture.R;
import com.whiteandc.capture.data.Monument;
import com.whiteandc.capture.data.MonumentList;

import java.io.File;
import java.lang.reflect.Field;

public class FragmentDescription  extends Fragment {
		private static final String CLASS = "FragmentDescription";
		private View rootView;
		

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			Log.i(CLASS+"->onCreateView", "Creando vista fragment description");
			
			rootView = inflater.inflate(R.layout.fragment_monument_captured, container, false);

			loadCurrentMonumentData();
	    
			return rootView;
		}

		public void loadCurrentMonumentData() {
			if (rootView != null){
				((MonumentsActivity) getParentFragment().getActivity()).onPageSelected(0);
				
				Monument monument= MonumentList.getMonument(((MonumentsActivity) getParentFragment().getActivity()).getCurrentMonumentId());

				//TextView textView = (TextView) rootView.findViewById(R.id.monument_name);
				//textView.setText(monument.getName());
				
				ImageView imgView= (ImageView) rootView.findViewById(R.id.capturedImage);
				
				
			    String path = Environment.getExternalStoragePublicDirectory(
		                Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/CaptureTheCity/";
			    Log.i(CLASS+"->loadCurrentMonumentData", path);
			    Drawable imgDrawable= Drawable.createFromPath(new File(path,monument.getCapturedImg()).getAbsolutePath());
			    if(imgDrawable!=null){
			    	imgView.setImageDrawable(imgDrawable);
			    }else{
			    	imgView.setImageResource(monument.getPhotos()[0]);
			    }
			    
				
				TextView descriptionView = (TextView) rootView.findViewById(R.id.monument_descroption);
				descriptionView.setText(monument.getDescription());
			}
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

	}