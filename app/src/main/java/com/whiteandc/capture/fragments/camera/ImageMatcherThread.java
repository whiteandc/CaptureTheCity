package com.whiteandc.capture.fragments.camera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.whiteandc.capture.CameraActivity;
import com.whiteandc.capture.opencv.OpenCVUtil;

import org.opencv.core.Mat;

public class ImageMatcherThread extends Thread{

    private static final String CLASS = "ImageMatcherThread";

    private long lastExecution = System.currentTimeMillis();
    private Activity activity;
    private Mat[] monuments;
    private Contract contract;

    public ImageMatcherThread(Activity activity, Contract contract, Mat[] monuments){
        Log.i(CLASS, "ImageMatcherThread.");
        this.activity = activity;
        this.contract = contract;
        this.monuments = monuments;
    }

    public void run(){
        while(!isInterrupted()){
            Mat currentFrame = contract.getFrame();
            try {
                if (currentFrame != null) {
                    boolean match = compare(currentFrame, monuments);
                    if (match) {
                        finishCaptured("Captured");
                    }
                } else {
                    // Sleep to avoid overuse of handset's resources
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        Log.e(CLASS, "Error during sleep.", e);
                    }
                }
            }finally {
                if (currentFrame != null) {
                    currentFrame.release();
                }
            }
        }
    }

    private void finishCaptured(final String msg) {
        activity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        ((CameraActivity) activity).finishActivity(Activity.RESULT_OK);
                    }
                }
        );
    }

    private boolean compare(Mat currentFrame, Mat[] monuments){
        boolean match = false;
        int i = 0;
        boolean storeResults = storeResults();
        while(monuments.length > i && !match){
            match = OpenCVUtil.compare2Images(currentFrame, monuments[i], storeResults, activity);
            i++;
        }
        return match;
    }

    private boolean storeResults() {
        long currentTime = System.currentTimeMillis();
        boolean storeResult = false;
        if(currentTime - lastExecution > 5000){
            storeResult = true;
            lastExecution = currentTime;
            Log.i(CLASS, "storeResult = " + lastExecution);
        }

        Log.i(CLASS, "storeResult = " + storeResult);
        return storeResult;
    }

    public interface Contract {
        public Mat getFrame();
    }
}
