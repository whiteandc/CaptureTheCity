package com.whiteandc.capture.fragments.list;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whiteandc.capture.R;
import com.whiteandc.capture.data.Monument;
import com.whiteandc.capture.data.MonumentList;

import java.util.ArrayList;

public class CityListAdapter extends ArrayAdapter<Monument> {
	private final Activity context;
	private LayoutInflater inflater;

	public CityListAdapter(Activity context) {
		super(context, R.layout.city_list, MonumentList.getList());
		this.context = context;
        this.inflater = context.getLayoutInflater();
	}
	  
	@Override
	public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        ArrayList<Monument> monuments = MonumentList.getList();
        Monument monument             = monuments.get(position);

        if(view == null){
            view = inflater.inflate(R.layout.city_list, parent, false);
            holder = new ViewHolder();

            // Setting the name of the item
            holder.text = (TextView) view.findViewById(R.id.item_txt);
            // Setting the image of the item
            holder.icon = (ImageView) view.findViewById(R.id.item_img);
            // Setting the number of pictures
           // holder.numPictures = (TextView) view.findViewById(R.id.num_pictures);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Jaapokki-Regular.otf");
        holder.text.setTypeface(font);
        holder.text.setText(monument.getName());
        holder.icon.setImageResource(monument.getPhotos()[0]);
        //holder.numPictures.setText(Integer.toString(monument.getPhotos().length));
		return view;
	}

    static class ViewHolder {
        TextView numPictures;
        TextView captured;
        TextView text;
        ImageView icon;
    }
}
