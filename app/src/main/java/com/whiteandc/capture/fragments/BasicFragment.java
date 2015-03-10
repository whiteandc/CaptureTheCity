package com.whiteandc.capture.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.whiteandc.capture.MonumentsActivity;

public class BasicFragment extends Fragment{

    protected MonumentsActivity monumentActivity;

    @Override
    public void onAttach(Activity activity) { //TODO extract super class
        super.onAttach(activity);
        this.monumentActivity = (MonumentsActivity) activity;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        this.monumentActivity = null;
    }
}
