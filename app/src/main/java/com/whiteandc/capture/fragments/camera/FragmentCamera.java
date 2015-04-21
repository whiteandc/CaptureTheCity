package com.whiteandc.capture.fragments.camera;


import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.commonsware.cwac.camera.ZoomTransaction;
import com.whiteandc.capture.MonumentsActivity;
import com.whiteandc.capture.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

public class FragmentCamera extends Fragment implements CameraBridgeViewBase.CvCameraViewListener2, ZoomManager.Contract, View.OnTouchListener, SeekBar.OnSeekBarChangeListener {

    private static final String PICTURE = "PICTURE";
    private static final String CLASS = "FragmentCamera";

    private Mat mRgba;
    private ScaleGestureDetector scaleGestureDetector;
    private int picture;
    private ImageView monumentPictureView;
    private OrientationEventListener myOrientationEventListener;
    private ImageView minView;
    private ImageView maxView;

    private JavaCameraView mOpenCvCameraView;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(getActivity()) {
        @Override
        public void onManagerConnected(int status) {
            if(status == LoaderCallbackInterface.SUCCESS){
                mOpenCvCameraView.enableView();
            }else{
                super.onManagerConnected(status);
            }
        }
    };

    public static FragmentCamera newInstance(int picture) {
        FragmentCamera f=new FragmentCamera();
        Bundle args=new Bundle();

        args.putInt(PICTURE, picture);
        f.setArguments(args);

        return(f);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MonumentsActivity monumentsActivity = (MonumentsActivity) getActivity();
        monumentsActivity.setFullScreen(true);
        monumentsActivity.setToolBarVisibility(false);
        monumentsActivity.setSelectedFragment(this);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        scaleGestureDetector = new ScaleGestureDetector(getActivity(), new ZoomManager(this));
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        picture = getArguments().getInt(PICTURE);
    }



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle bundle) {
        super.onCreateView(inflater, container, bundle);

        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        rootView.setOnTouchListener(this);

        mOpenCvCameraView = (JavaCameraView) rootView.findViewById(R.id.camera_surface_view);
        mOpenCvCameraView.setCvCameraViewListener(this);

        monumentPictureView = (ImageView) rootView.findViewById(R.id.transparent_monument);

        minView = (ImageView) rootView.findViewById(R.id.transparence_min);
        maxView = (ImageView) rootView.findViewById(R.id.transparence_max);
        initOrientationListener();

        Drawable pictureDrawable = getResources().getDrawable(picture);
        if(isLandscape(pictureDrawable)){
            rotateImage(monumentPictureView);
        }else{
            monumentPictureView.setImageDrawable(pictureDrawable);
        }

        SeekBar transparenceSeekBar = (SeekBar) rootView.findViewById(R.id.transparence);
        transparenceSeekBar.setOnSeekBarChangeListener(this);
        transparenceSeekBar.setProgress(128);

        return(rootView);
    }

    @Override
    public void onResume(){
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_10, getActivity(), mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myOrientationEventListener.disable();
        mOpenCvCameraView.disableView();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    /**
     * This class send all the touch events from the camera view to the scaleGestureDetector
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public void zoomTo(int level) {
        Camera.Parameters parameters = mOpenCvCameraView.getCameraParameters();
        parameters.setZoom(level);
        mOpenCvCameraView.setCameraParameters(parameters);
    }

    @Override
    public int getMaxZoom() {
        Camera.Parameters parameters = mOpenCvCameraView.getCameraParameters();
        return parameters.getMaxZoom();
    }

    @Override
    public boolean doesZoomReallyWork() {
        return mOpenCvCameraView.getCameraParameters().isZoomSupported();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float alpha = (float) progress / 255f;
        monumentPictureView.setAlpha(alpha);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        return mRgba;
    }

    private void rotateImage(ImageView pictureView) {
        Bitmap bmpOriginal = BitmapFactory.decodeResource(this.getResources(), picture);
        Bitmap bmResult = Bitmap.createBitmap(bmpOriginal.getWidth(), bmpOriginal.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(bmResult);
        tempCanvas.rotate(90, bmpOriginal.getWidth() / 2, bmpOriginal.getHeight() / 2);
        tempCanvas.drawBitmap(bmpOriginal, 0, 0, null);

        bmpOriginal.recycle();

        pictureView.setImageBitmap(bmResult);
    }

    private boolean isLandscape(Drawable pictureDrawable) {
        return pictureDrawable.getIntrinsicHeight() < pictureDrawable.getIntrinsicWidth();
    }

    private void initOrientationListener() {
        View[] views = new View[]{minView, maxView};
        myOrientationEventListener
                = new ViewOrientationListener(getActivity(), SensorManager.SENSOR_DELAY_NORMAL, views);

        if (myOrientationEventListener.canDetectOrientation()){
            myOrientationEventListener.enable();
        }
    }
}