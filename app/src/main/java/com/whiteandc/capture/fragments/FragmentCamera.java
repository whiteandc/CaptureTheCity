package com.whiteandc.capture.fragments;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.whiteandc.capture.MonumentsActivity;
import com.whiteandc.capture.MyDialog;
import com.whiteandc.capture.R;
import com.whiteandc.capture.ShowCamera;
import com.whiteandc.capture.data.MonumentList;
import com.whiteandc.capture.opencv.OpenCVUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FragmentCamera extends Fragment implements OnClickListener {
	
	private static final String CLASS = "FragmentCamera";
	private Camera cameraObject;
	private ShowCamera showCamera;
	private Button button;
	   
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		getActivity().getActionBar().hide();
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		View v = inflater.inflate(R.layout.fragment_camera, container, false);
		
		RelativeLayout preview = (RelativeLayout) v.findViewById(R.id.preview);
	    	    
	    int currentImg = ((MonumentsActivity) getActivity()).getCurrentImg();
	    
	    ImageView transparentImg =new ImageView(getActivity());    
	    
	    // find the width and height of the screen:
	    Display d = getActivity().getWindowManager().getDefaultDisplay();
	    int screenWidth = d.getWidth(); 
	    int screenHeight = d.getHeight();
	    Log.i(CLASS, "X = "+screenWidth+"   Y = "+screenHeight);

	    Bitmap bMap = BitmapFactory.decodeResource(this.getResources(), currentImg);
	    Matrix mat = new Matrix();
	    Log.i(CLASS, "y/bMap.getWidth() = "+(float) screenHeight/bMap.getWidth()+"   x/bMap.getHeight() = "+(float)screenWidth/bMap.getHeight());
	    Log.i(CLASS, "bMap.getWidth() = "+bMap.getWidth()+"   bMap.getHeight() = "+bMap.getHeight());
	    if(bMap.getWidth()>bMap.getHeight()){
	    	mat.setScale((float) screenWidth/bMap.getHeight(),(float)screenWidth/bMap.getHeight()); 
	  	    mat.postRotate(90);
	    }else{
	    	mat.setScale((float) screenWidth/bMap.getWidth(),(float)screenWidth/bMap.getWidth()); 
	    }

	    Bitmap bMapRotate = Bitmap.createBitmap(bMap, 0, 0,
        bMap.getWidth(), bMap.getHeight(), mat, true); 
	    transparentImg.setImageBitmap(bMapRotate);
	    
	    cameraObject = getCameraInstance();
	    showCamera = new ShowCamera(getActivity(), cameraObject);
	    
	    preview.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	    transparentImg.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	    
	    RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
	    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
	    showCamera.setLayoutParams(layoutParams);
	    
	    preview.addView(showCamera);
	    int marginWidth = (screenHeight-bMapRotate.getHeight())/2;
	    Log.i(CLASS, "screenHeight = "+screenHeight+"   bMapRotate.getHeight() = "+bMapRotate.getHeight()+"   marginWidth = "+marginWidth);
	    
	    drawBlackMargin(preview, marginWidth, RelativeLayout.ALIGN_PARENT_TOP);
	    drawBlackMargin(preview, marginWidth, RelativeLayout.ALIGN_PARENT_BOTTOM);
	    
	    drawButton(preview, marginWidth);
	    
	    transparentImg.setAlpha(0.5f);     
	    transparentImg.setScaleType(ScaleType.CENTER);
	    preview.addView(transparentImg);
 
        return v;
	}


	private void drawButton(RelativeLayout preview, int marginWidth) {
		button= new Button(getActivity());
	    button.setText("Capture");
	    RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, marginWidth);
	    layoutParams.addRule( RelativeLayout.ALIGN_PARENT_BOTTOM);
	    layoutParams.addRule( RelativeLayout.CENTER_HORIZONTAL);
	    button.setLayoutParams(layoutParams);
	    button.setOnClickListener(this);
	    preview.addView(button);
	}


	private void drawBlackMargin(RelativeLayout preview, int marginWidth, int gravity) {
		ImageView margenSup = new ImageView(getActivity());
	    margenSup.setBackgroundColor(Color.BLACK);
	    RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, marginWidth);
	    layoutParams.addRule(gravity);
	    margenSup.setLayoutParams(layoutParams);
	    preview.addView(margenSup);
	} 
	
	private void showDialog(String title, String msg) {
        FragmentManager fm = getParentFragment().getChildFragmentManager();
        MyDialog dialog = new MyDialog();
        dialog.setTitle(title);
        dialog.setText(msg);
        dialog.show(fm, "fragment_edit_name");
    }
	
	private PictureCallback capturedIt = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
            StringBuilder debugInfo = new StringBuilder("INFORMACION DE LA CAPTURA: \n");
            boolean equals = false;
            
            //TODO Lanzar un icono de cargando..
            equals = compareCapturedImage(data, debugInfo, equals);
            
            
            releaseCameraAndPreview();
            // Las dos imagenes son la misma
            if (equals){
            	  showDialog("Enhorabuena!", "Has capturado un monumento");
            	  saveData();
            	  FragmentTransaction transaction =  getParentFragment().getChildFragmentManager().beginTransaction();
            	  transaction.replace(R.id.layoutToReplace, ((MonumentsActivity) getActivity()).fragmentMonument.fragmentDescription).commit();
            }else{
            	showDialog("Error", "La captura ha fallado, por favor intentelo de nuevo");
        		FragmentTransaction transaction =  getParentFragment().getChildFragmentManager().beginTransaction();

        		transaction.replace(R.id.layoutToReplace, ((MonumentsActivity) getActivity()).fragmentMonument.fragmentGallery).commit();
            }
        }

		private void saveData() {
			String monumentName= ((MonumentsActivity) getActivity()).getCurrentMonumentId();
			MonumentList.monumentList.get(monumentName).setIsCaptured(true);
			Log.i(CLASS, "Almacenando en Shared Prefrences monumento "+monumentName+" CAPTURADO");
			SharedPreferences sharedpreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
			Editor editor = sharedpreferences.edit();
			editor.putString(monumentName, "true");
			editor.commit();
		}

		private boolean compareCapturedImage(byte[] data,
				StringBuilder debugInfo, boolean equals) {
			File capturedImgFile = getOutputMediaFile();
			try { 
	            if (capturedImgFile == null){
	                Toast.makeText(getActivity(), "Image retrieval failed.", Toast.LENGTH_SHORT).show();
	                throw new Exception();
	            } 
                FileOutputStream fos = new FileOutputStream(capturedImgFile);
                fos.write(data);
                fos.close();
               
                String filename2= getActivity().getResources().getResourceName(((MonumentsActivity) getActivity()).getCurrentImg());
                int currentImg = ((MonumentsActivity) getActivity()).getCurrentImg(); 
                
                debugInfo.append("Nombre imagen capturada: "+capturedImgFile+"\n"
                				+  "Nombre imagen a reproducir: "+filename2+"\n");
                equals = OpenCVUtil.compare2Images(capturedImgFile.getPath(), currentImg, getActivity(), debugInfo);

                debugInfo.append("Las imagenes "+(equals ? "" : "NO")+" son iguales.");
                Toast.makeText(getActivity(), "Son iguales: "+ equals, Toast.LENGTH_LONG).show();
                
                // Restart the camera preview.
                safeCameraOpenInView(showCamera);
                
            } catch (Exception e) {
            	Log.e(CLASS, "Error: ",e);
            }finally{
            	File debugFile = getOutputTxtFile();
            	FileOutputStream fos;
				try {
					fos = new FileOutputStream(debugFile);
					fos.write(debugInfo.toString().getBytes());
					fos.close();
				} catch (Exception e) {Log.e(CLASS, "Error: ",e);}

            	Log.i(CLASS, debugInfo.toString());
            	getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(capturedImgFile)));
            }
			return equals;
		}
	};  
	
    private boolean safeCameraOpenInView(View view) {
        boolean qOpened = false;
        releaseCameraAndPreview();
        cameraObject = getCameraInstance();
        //showCamera = (ShowCamera) view;
        qOpened = (cameraObject != null);

        if(qOpened == true){
           // showCamera = new ShowCamera(getActivity(), cameraObject);
            //RelativeLayout preview = (RelativeLayout) view.findViewById(R.id.preview);
            //preview.addView(showCamera);
            //showCamera.surfaceCreated(showCamera.getHolder());
        }
        return qOpened;
    }
    
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return c; // returns null if camera is unavailable
    } 
	
    
    private File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CaptureTheCity");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Camera Guide", "Required media storage does not exist");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
        
        Toast.makeText(getActivity(), "Picture saved", Toast.LENGTH_LONG).show();;
        return mediaFile;
    }

    
    private File getOutputTxtFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CaptureTheCity");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Camera Guide", "Required media storage does not exist");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "DEBUG_"+ timeStamp + ".txt");

        Toast.makeText(getActivity(), "Picture saved", Toast.LENGTH_LONG).show();;
        return mediaFile;
    }
    
	public void snapIt(View view) {
		cameraObject.takePicture(null, null, capturedIt);
	}
	
	
    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseCameraAndPreview();
    }

    
    /**
     * Clear any existing preview / camera.
     */
    private void releaseCameraAndPreview() {

        if (cameraObject != null) {
            cameraObject.stopPreview();
            cameraObject.release();
            cameraObject = null;
        }
        if(showCamera != null){
            showCamera.destroyDrawingCache();
            showCamera.theCamera = null;
        }
    }


	@Override
	public void onClick(View v) {
		if(v.equals(button)){
			snapIt(null);
		}
		
	} 
	
	@Override
	public void onDetach() {
	    super.onDetach();

	    try {
	        Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
	        childFragmentManager.setAccessible(true);
	        childFragmentManager.set(this, null);

	    } catch (NoSuchFieldException e) {
	        throw new RuntimeException(e);
	    } catch (IllegalAccessException e) {
	        throw new RuntimeException(e);
	    }
	}

}
