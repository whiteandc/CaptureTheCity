package com.whiteandc.capture.fragments.camera;


import android.annotation.TargetApi;
import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.Toast;
import com.commonsware.cwac.camera.CameraUtils;
import com.commonsware.cwac.camera.SimpleCameraHost;
import com.commonsware.cwac.camera.PictureTransaction;

class CameraHost extends SimpleCameraHost implements Camera.FaceDetectionListener {

    private FragmentCamera mfragmentCamera;
    private Activity mActivity;
    private boolean supportsFaces = false;
    private Bundle mBundle;
    private static final String KEY_USE_FFC=
            "com.commonsware.cwac.camera.demo.USE_FFC";
    private MenuItem singleShotItem=null;
    private MenuItem autoFocusItem=null;
    private MenuItem takePictureItem=null;
    private MenuItem mirrorFFC=null;
    private long lastFaceToast=0L;
    private boolean singleShotProcessing=false;
    private String flashMode=null;

    public CameraHost(Activity activity, Bundle bundle, FragmentCamera fragmentCamera) {
        super(activity);
        mActivity = activity;
        mBundle = bundle;
        mfragmentCamera = fragmentCamera;
    }

    @Override
    public boolean useFrontFacingCamera() {
        if (mBundle == null) {
            return false;
        }

        return (mBundle.getBoolean(KEY_USE_FFC));
    }

    @Override
    public boolean useSingleShotMode() {
        if (singleShotItem == null) {
            return (false);
        }

        return (singleShotItem.isChecked());
    }

    @Override
    public void saveImage(PictureTransaction xact, byte[] image) {
        if (useSingleShotMode()) {
            singleShotProcessing = false;

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    takePictureItem.setEnabled(true);
                }
            });

        } else {
            super.saveImage(xact, image);
        }
    }

    @Override
    public void autoFocusAvailable() {
        if (autoFocusItem != null) {
            autoFocusItem.setEnabled(true);

            if (supportsFaces)
                mfragmentCamera.startFaceDetection();
        }
    }

    @Override
    public void autoFocusUnavailable() {
        if (autoFocusItem != null) {
            mfragmentCamera.stopFaceDetection();

            if (supportsFaces)
                autoFocusItem.setEnabled(false);
        }
    }

    @Override
    public void onCameraFail(CameraHost.FailureReason reason) {
        super.onCameraFail(reason);

        Toast.makeText(mActivity,
                       "Sorry, but you cannot use the camera now!",
                       Toast.LENGTH_LONG).show();
    }

    @Override
    public Parameters adjustPreviewParameters(Parameters parameters) {
        flashMode =
                CameraUtils.findBestFlashModeMatch(parameters,
                        Camera.Parameters.FLASH_MODE_RED_EYE,
                        Camera.Parameters.FLASH_MODE_AUTO,
                        Camera.Parameters.FLASH_MODE_ON);

        if (parameters.getMaxNumDetectedFaces() > 0) {
            supportsFaces = true;
        } else {
            Toast.makeText(mActivity    ,
                    "Face detection not available for this camera",
                    Toast.LENGTH_LONG).show();
        }

        return (super.adjustPreviewParameters(parameters));
    }

    @Override
    public void onFaceDetection(Face[] faces, Camera camera) {
        if (faces.length > 0) {
            long now = SystemClock.elapsedRealtime();

            if (now > lastFaceToast + 10000) {
                Toast.makeText(mActivity, "I see your face!",
                        Toast.LENGTH_LONG).show();
                lastFaceToast = now;
            }
        }
    }

    @Override
    @TargetApi(16)
    public void onAutoFocus(boolean success, Camera camera) {
        super.onAutoFocus(success, camera);

        takePictureItem.setEnabled(true);
    }

    @Override
    public boolean mirrorFFC() {
        return (mirrorFFC.isChecked());
    }
}
