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
    private FrameProvider frameProvider;

    public ImageMatcherThread(Activity activity, FrameProvider frameProvider, Mat[] monuments){
        this.activity = activity;
        this.frameProvider = frameProvider;
        this.monuments = monuments;
    }

    public void run(){
        while(!isInterrupted()){
            Mat currentFrame = frameProvider.getFrame();
            if(currentFrame != null) {
                boolean match = compare(currentFrame, monuments);
                if (match) {
                    executeToast("Captured");
                }
            }else{
                // Sleep to avoid overuse of handset's resources
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    Log.e(CLASS, "Error during sleep.", e);
                }
            }
        }
    }

    private void executeToast(final String msg) {
        activity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private boolean compare(Mat currentFrame, Mat[] monuments){
        boolean match = false;
        int i = 0;
        boolean storeResults = storeResults();
        while(monuments.length > i && !match){
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
        return storeResult;
    }

    public interface FrameProvider{
        public Mat getFrame();
    }
}
