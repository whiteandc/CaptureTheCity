package com.whiteandc.capture.fragments.camera;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class ShowCamera extends SurfaceView implements SurfaceHolder.Callback {

   private SurfaceHolder holdMe;
   public Camera theCamera;
   private Activity activity;
   private List<Size> mSupportedPreviewSizes;
private Size mPreviewSize; 

   public ShowCamera(Activity activity,Camera camera) {
      super(activity);
      theCamera = camera;
      holdMe = getHolder();
      holdMe.addCallback(this);
      this.activity = activity;
      
      mSupportedPreviewSizes = theCamera.getParameters().getSupportedPreviewSizes();
   }

   @Override
   public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
	   setCameraDisplayOrientation();
   }

   @Override
   public void surfaceCreated(SurfaceHolder holder) {
	  Log.i("ShowCamera->surfaceCreated()", "surfaceCreated");
      try   {
    	  theCamera.setPreviewDisplay(holder);
    	  Log.i("ShowCamera->surfaceCreated()", "width = "+mPreviewSize.width+", height = "+mPreviewSize.height);
		  Camera.Parameters parameters = theCamera.getParameters();
		  parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
		  theCamera.setParameters(parameters);
		  theCamera.startPreview();
      } catch (IOException e) {
    	  Log.e("ShowCamera->surfaceCreated() ", "Error: \n", e);
      }
   } 

   @Override
   public void surfaceDestroyed(SurfaceHolder arg0) {
	   Log.i("ShowCamera->surfaceDestroyed()", "surfaceDestroyed");
   }
   
	private int getDefaultCameraId() {
		int defaultCameraId = 0;
		// Find the total number of cameras available
		int numberOfCameras = Camera.getNumberOfCameras();
		// Find the ID of the default camera
		CameraInfo cameraInfo = new CameraInfo();
		for (int i = 0; i < numberOfCameras; i++) {
			Camera.getCameraInfo(i, cameraInfo);
			if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
				defaultCameraId = i;
			}
		}
		
		return defaultCameraId;
	}
	   
	public void setCameraDisplayOrientation() {


		CameraInfo info = new CameraInfo();

		Camera.getCameraInfo(getDefaultCameraId(), info);

		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
		int degrees = 0;

		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		int result;
		if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		theCamera.setDisplayOrientation(result);
	}



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	Log.i("ShowCamera->onMeasure()", "onMeasure");
        //final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        //final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);

	    // find the width and height of the screen:
	    Display d = activity.getWindowManager().getDefaultDisplay();
	    int width = d.getWidth();
	    int height = d.getHeight();
        Log.i("ShowCamera->onMeasure()", "width = "+width+"  height = "+height);

        if (mSupportedPreviewSizes != null) {
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
        }

        Log.i("ShowCamera->onMeasure()", "Optimal preview width = "+mPreviewSize.width+"  height = "+mPreviewSize.height);

        float ratio;
        if(mPreviewSize.height >= mPreviewSize.width)
            ratio = (float) mPreviewSize.height / (float) mPreviewSize.width;
        else
            ratio = (float) mPreviewSize.width / (float) mPreviewSize.height;

        Log.i("ShowCamera->onMeasure()", "Ratio = "+ratio);

        Log.i("ShowCamera->onMeasure()", "Final width = "+width+"  height = "+(int) (width * ratio));
        // One of these methods should be used, second method squishes preview slightly
        setMeasuredDimension(width, (int) (width * ratio));
//        setMeasuredDimension((int) (width * ratio), height);
    }


	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio=(double)h / w;

        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }


}