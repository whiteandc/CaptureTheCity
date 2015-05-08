package com.whiteandc.capture;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.whiteandc.capture.fragments.camera.FragmentCamera;

public class CameraActivity extends FragmentActivity {
    private static final String CLASS = "CameraActivity";
    private int currentPicture;
    private String monumentId;
    private int pictureResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Bundle extras   = getIntent().getExtras();
        currentPicture  = extras.getInt(Assets.CURRENT_PICTURE);
        pictureResource = extras.getInt(Assets.PICTURE_RESOURCE);
        monumentId      = extras.getString(Assets.MONUMENT_ID);

        switchToFragmentCamera(pictureResource);
        Log.i(CLASS, "currentPicture: " + currentPicture +
                    " monumentId: " + monumentId +
                    " pictureResource: " + pictureResource);
    }

    public void switchToFragmentCamera(int currentPicture) {
        final FragmentCamera fragmentCamera = FragmentCamera.newInstance(currentPicture);
        switchFragment(fragmentCamera);
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, fragment);
        ft.addToBackStack("back");
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        finishActivity(RESULT_CANCELED);
    }

    public void finishActivity(int result) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Assets.CURRENT_PICTURE, currentPicture);
        returnIntent.putExtra(Assets.MONUMENT_ID, monumentId);
        returnIntent.putExtra(Assets.PICTURE_RESOURCE, pictureResource);
        setResult(result, returnIntent);
        finish();
    }
}
