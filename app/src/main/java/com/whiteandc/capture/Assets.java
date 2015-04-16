package com.whiteandc.capture;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by carlos on 3/15/2015.
 */
public class Assets {
    public static void setFont1(TextView textView, Context context){
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Jaapokki-Regular.otf");
        textView.setTypeface(font);
    }
}
