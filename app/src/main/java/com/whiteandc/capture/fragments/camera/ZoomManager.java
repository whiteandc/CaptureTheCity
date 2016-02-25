package com.whiteandc.capture.fragments.camera;

import android.util.Log;
import android.view.ScaleGestureDetector;

import com.whiteandc.capture.exceptions.CameraParameterNotReadyException;


public class ZoomManager extends ScaleGestureDetector.SimpleOnScaleGestureListener{
    private static final String CLASS = "ZoomManager";
    private static final int NUM_STEP = 15;

    private Contract contractFulfiller;
    private int progress = 0;

    public ZoomManager(Contract contractFulfiller){
        this.contractFulfiller = contractFulfiller;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scaleFactor = detector.getScaleFactor();
        try {
            if (contractFulfiller.doesZoomReallyWork()) {
                getProgress(scaleFactor);
                contractFulfiller.zoomTo(progress);
            }
        }catch(CameraParameterNotReadyException e){
            Log.w(CLASS, "Camera properties unavailable", e);
        }

        return true;
    }

    private void getProgress(float scaleFactor) {
        int maxZoom = contractFulfiller.getMaxZoom();

        int step = maxZoom / NUM_STEP;

        if (step == 0){
            step = 1;
        }

        if(scaleFactor > 1. && progress < maxZoom){
            progress = progress + step;
        }else if(scaleFactor < 1. && progress > 0){
            progress = progress - step;
        }

        if(progress > maxZoom){
            progress = maxZoom;
        }

        if(progress < 0){
            progress = 0;
        }
    }

    public interface Contract{
        public void zoomTo(int level);
        public int getMaxZoom();
        public boolean doesZoomReallyWork();
    }
}
