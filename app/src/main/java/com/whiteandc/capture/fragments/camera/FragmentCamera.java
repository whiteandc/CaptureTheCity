package com.whiteandc.capture.fragments.camera;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.hardware.Camera.Parameters;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import com.commonsware.cwac.camera.CameraFragment;
import com.commonsware.cwac.camera.CameraHost;
import com.commonsware.cwac.camera.CameraUtils;
import com.commonsware.cwac.camera.SimpleCameraHost;
import com.commonsware.cwac.camera.PictureTransaction;
import com.whiteandc.capture.MonumentsActivity;
import com.whiteandc.capture.R;
import com.whiteandc.capture.exceptions.CameraParameterNotReadyException;

public class FragmentCamera extends CameraFragment implements View.OnTouchListener, ZoomManager.Contract, OnSeekBarChangeListener {
    private static final String KEY_USE_FFC = "com.commonsware.cwac.camera.demo.USE_FFC";
    private static final String PICTURE = "com.whiteandc.capture.fragments.camera.picture";
    private static final String CLASS = "FragmentCamera";
    private long lastFaceToast=0L;
    String flashMode=null;
    private DemoCameraHost cameraHost;
    private ScaleGestureDetector scaleGestureDetector;
    private int picture;
    ImageView pictureView;
    private OrientationEventListener myOrientationEventListener;
    private ImageView minView;
    private ImageView maxView;

    private enum Position{
        UP, RIGHT, DOWN, LEFT
    }

    private Position position = Position.UP;

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

        scaleGestureDetector = new ScaleGestureDetector(getActivity(), new ZoomManager(this));
    }

    public FragmentCamera() {
        super();
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        myOrientationEventListener
                = new OrientationEventListener(getActivity(), SensorManager.SENSOR_DELAY_NORMAL){

            @Override
            public void onOrientationChanged(int angle) {
                Position previousPosition = position;

                if (angle >= 315 || angle < 45){
                    position = Position.UP;
                    rotate(position, previousPosition);
                }else if (angle >= 45 && angle < 135){
                    position = Position.RIGHT;
                    rotate(position, previousPosition);
                }else if (angle >= 135 && angle < 225){
                    position = Position.DOWN;
                    rotate(position, previousPosition);
                }else if (angle >= 225 && angle < 315){
                    position = Position.LEFT;
                    rotate(position, previousPosition);
                }

                Log.i(CLASS, "angle: " + angle + "  " + "current: " + position + "  " + "previous: "+previousPosition);
            }

            private void rotate(Position position, Position previousPosition) {
                // There is an easier way but this works with older Android versions
                RotateAnimation anim = new RotateAnimation(positionToDegrees(previousPosition), positionToDegrees(position),
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

                anim.setInterpolator(new LinearInterpolator());
                anim.setDuration(1500);
                anim.setFillEnabled(true);

                anim.setFillAfter(true);
                if (minView != null) {
                    minView.startAnimation(anim);
                }

                if (maxView != null) {
                    maxView.startAnimation(anim);
                }
            }

            private int positionToDegrees(Position pos){
                switch (pos){
                    case UP:
                        return 0;
                    case RIGHT:
                        return -90;
                    case DOWN:
                        return 180;
                    case LEFT:
                        return 90;
                    default:
                        return 0;
                }
            }
        };

        if (myOrientationEventListener.canDetectOrientation()){
            myOrientationEventListener.enable();
        }

        picture = getArguments().getInt(PICTURE);
        setHasOptionsMenu(true);

        cameraHost = new DemoCameraHost(getActivity());
        SimpleCameraHost.Builder builder=
                new SimpleCameraHost.Builder(cameraHost);

        setHost(builder.useFullBleedPreview(true).build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle bundle) {
        View cameraView=
                super.onCreateView(inflater, container, bundle);

        cameraView.setOnTouchListener(this);
        View results=inflater.inflate(R.layout.fragment_camera, container, false);
        ((ViewGroup)results.findViewById(R.id.camera)).addView(cameraView);

        pictureView = (ImageView) results.findViewById(R.id.transparent_monument);

        minView = (ImageView) results.findViewById(R.id.transparence_min);
        maxView = (ImageView) results.findViewById(R.id.transparence_max);

        Drawable pictureDrawable = getResources().getDrawable(picture);
        if(isLandscape(pictureDrawable)){
            rotateImage(pictureView);
        }else{
            pictureView.setImageDrawable(pictureDrawable);
        }

        SeekBar transparenceSeekBar = (SeekBar) results.findViewById(R.id.transparence);
        transparenceSeekBar.setOnSeekBarChangeListener(this);
        transparenceSeekBar.setProgress(128);

        return(results);
    }

    private void rotateImage(ImageView pictureView) {
        Bitmap bmpOriginal = BitmapFactory.decodeResource(this.getResources(), picture);
        Bitmap bmResult = Bitmap.createBitmap(bmpOriginal.getWidth(), bmpOriginal.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(bmResult);
        tempCanvas.rotate(90, bmpOriginal.getWidth() / 2, bmpOriginal.getHeight() / 2);
        tempCanvas.drawBitmap(bmpOriginal, 0, 0, null);

        pictureView.setImageBitmap(bmResult);
    }




    private boolean isLandscape(Drawable pictureDrawable) {
        return pictureDrawable.getIntrinsicHeight() < pictureDrawable.getIntrinsicWidth();
    }

    void takeSimplePicture() {
        PictureTransaction xact=new PictureTransaction(getHost());

//        if (flashItem!=null && flashItem.isChecked()) {
//            xact.flashMode(flashMode);
//        }

        takePicture(xact);
    }

    /**
     * This class send all the touch events from the camera view to the scaleGestureDetector
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i(CLASS, "onTouch");
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public int getMaxZoom() {
        return cameraHost.getMaxZoom();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float alpha = (float) progress / 255f;
        Log.i(CLASS, ""+alpha);
        pictureView.setAlpha(alpha);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myOrientationEventListener.disable();
    }

    class DemoCameraHost extends SimpleCameraHost implements
            Camera.FaceDetectionListener {
        boolean supportsFaces=false;
        private Parameters cameraParameters;

        public DemoCameraHost(Context _ctxt) {
            super(_ctxt);
        }

        @Override
        public boolean useFrontFacingCamera() {
            if (getArguments() == null) {
                return(false);
            }

            return(getArguments().getBoolean(KEY_USE_FFC));
        }

        @Override
        public void saveImage(PictureTransaction xact, byte[] image) {
            super.saveImage(xact, image);
        }

        @Override
        public void autoFocusAvailable() {
            if (supportsFaces)
                startFaceDetection();

        }

        @Override
        public void autoFocusUnavailable() {

        }

        @Override
        public void onCameraFail(CameraHost.FailureReason reason) {
            super.onCameraFail(reason);

            Toast.makeText(getActivity(),
                    "Sorry, but you cannot use the camera now!",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public Parameters adjustPreviewParameters(Parameters parameters) {
            cameraParameters = parameters;
            flashMode=
                    CameraUtils.findBestFlashModeMatch(parameters,
                            Camera.Parameters.FLASH_MODE_RED_EYE,
                            Camera.Parameters.FLASH_MODE_AUTO,
                            Camera.Parameters.FLASH_MODE_ON);

            if (parameters.getMaxNumDetectedFaces() > 0) {
                supportsFaces=true;
            }
            else {
                Toast.makeText(getActivity(),
                        "Face detection not available for this camera",
                        Toast.LENGTH_LONG).show();
            }

            return(super.adjustPreviewParameters(parameters));
        }

        @Override
        public void onFaceDetection(Face[] faces, Camera camera) {
            if (faces.length > 0) {
                long now=SystemClock.elapsedRealtime();

                if (now > lastFaceToast + 10000) {
                    Toast.makeText(getActivity(), "I see your face!",
                            Toast.LENGTH_LONG).show();
                    lastFaceToast=now;
                }
            }
        }

        @Override
        @TargetApi(16)
        public void onAutoFocus(boolean success, Camera camera) {
            super.onAutoFocus(success, camera);
        }

        @Override
        public boolean mirrorFFC() {
            return(false);
        }

        public int getMaxZoom(){
            int maxZoom = 0;
            if(cameraParameters != null){
                maxZoom = cameraParameters.getMaxZoom();
            }else{
                throw new CameraParameterNotReadyException(
                        "Parameter MaxZoom could not be read, camera's parameters are not ready.");
            }

            return maxZoom;
        }
    }
}