package com.whiteandc.capture.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whiteandc.capture.MonumentsActivity;

public abstract class BasicFragment extends Fragment{

    protected MonumentsActivity monumentActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        monumentActivity = (MonumentsActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle bundle) {
        View view = super.onCreateView(inflater,container,bundle);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.monumentActivity = null;
    }
}
