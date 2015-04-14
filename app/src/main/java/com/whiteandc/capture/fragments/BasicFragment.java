package com.whiteandc.capture.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.whiteandc.capture.MonumentsActivity;

public abstract class BasicFragment extends Fragment{

    protected MonumentsActivity monumentActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        monumentActivity = (MonumentsActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.monumentActivity = null;
    }
}
