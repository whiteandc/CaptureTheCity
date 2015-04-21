package com.whiteandc.capture.fragments.camera;

import android.content.Context;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * Created by crubio on 4/18/2015.
 */
public class ViewOrientationListener extends OrientationEventListener {

    public static final int ANIMATION_DURATION = 1500;

    private enum Position{
        UP, RIGHT, DOWN, LEFT
    }

    private Position position = Position.UP;
    private View[] views;

    public ViewOrientationListener(Context context, int rate, View[] views){
        super(context, rate);
        this.views = views;
    }

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

    }

    private void rotate(Position position, Position previousPosition) {
        // There is an easier way but this works with older Android versions
        RotateAnimation anim = new RotateAnimation(positionToDegrees(previousPosition), positionToDegrees(position),
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(ANIMATION_DURATION);
        anim.setFillEnabled(true);
        anim.setFillAfter(true);

        for (int i = 0; i < views.length; i++) {
            if (views[i] != null) {
                views[i].startAnimation(anim);
            }
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
}
