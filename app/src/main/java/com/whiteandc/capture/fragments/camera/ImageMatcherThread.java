package com.whiteandc.capture.fragments.camera;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

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
            if(currentFrame != null) {
                Log.i(CLASS, "currentFrame != null. Time: "+System.currentTimeMillis());
                boolean match = compare(currentFrame, monuments);
                if (match) {
                    finishCaptured("Captured");
                }
            }else{
                Log.i(CLASS, "currentFrame == null.");
                // Sleep to avoid overuse of handset's resources
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    Log.e(CLASS, "Error during sleep.", e);
                }
            }
        }
    }

    private void finishCaptured(final String msg) {
        activity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        contract.finishActivity(Activity.RESULT_OK);
                    }
                }
        );
    }

    private boolean compare(Mat currentFrame, Mat[] monuments){
        boolean match = false;
        int i = 0;
        boolean storeResults = storeResults();
        while(monuments.length > i && !match){
            Log.i(CLASS, "start compare");
            match = OpenCVUtil.compare2Images(currentFrame, monuments[i], storeResults);
        }
        return match;
    }

    private boolean storeResults() {
        long currentTime = System.currentTimeMillis();
        boolean storeResult = false;
        if(currentTime - lastExecution > 1000){
            storeResult = true;
            lastExecution = currentTime;
        }

        Log.i(CLASS, "storeResult = " + storeResult);
        return storeResult;
    }

    public interface Contract {
        public Mat getFrame();
        public void finishActivity(int status);
    }
}
