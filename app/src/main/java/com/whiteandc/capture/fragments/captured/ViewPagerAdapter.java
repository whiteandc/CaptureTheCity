package com.whiteandc.capture.fragments.captured;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.whiteandc.capture.R;


public class ViewPagerAdapter extends PagerAdapter {
    // Declare Variables
    private Context context;
    private int[] monument;
    private LayoutInflater inflater;
 
    public ViewPagerAdapter(Context context,  int[] monuments) {
        this.context = context;
        this.monument = monuments;

    }
 
    @Override
    public int getCount() {
        return monument.length;
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }
 
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item, container,
                false);
        // Locate the ImageView in viewpager_item.xml
        ImageView img = (ImageView) itemView.findViewById(R.id.img_monument);
        // Capture position and set to the ImageView
        img.setImageResource(monument[position]);
 
        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);
 
        return itemView;
    }

 
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((LinearLayout) object);
    }

	public void setMonument(int[] monument) {
		this.monument = monument;
	}

}