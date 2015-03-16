package com.whiteandc.capture.fragments.list;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whiteandc.capture.Assets;
import com.whiteandc.capture.R;
import com.whiteandc.capture.data.Monument;
import com.whiteandc.capture.data.MonumentList;

import java.util.ArrayList;

/**
 *  Para Blanca: http://developer.android.com/training/improving-layouts/smooth-scrolling.html
 *  Aqui explica para que se usa el viewHolder y el Asynctask
 *  http://developer.android.com/reference/android/os/AsyncTask.html
 */
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

        if(view == null){
            view = inflater.inflate(R.layout.city_list, parent, false);
            holder = new ViewHolder();

            // Setting the name of the item
            holder.text = (TextView) view.findViewById(R.id.item_txt);
            // Setting the image of the item
            holder.icon = (ImageView) view.findViewById(R.id.item_img);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.position = position;
        new ThumbnailTask(position, holder, context)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
		return view;
	}

    private static class ThumbnailTask extends AsyncTask<ViewHolder, Void, Drawable> {
        private int mPosition;
        private ViewHolder mHolder;
        private Activity mContext;
        private Monument mMonument;

        public ThumbnailTask(int position, ViewHolder holder, Activity context) {
            mPosition = position;
            mHolder = holder;
            mContext = context;
        }

        @Override
        //We should download the pictures here
        protected Drawable doInBackground(ViewHolder... params) {
            ArrayList<Monument> monuments = MonumentList.getList();
            mMonument = monuments.get(mPosition);
            Drawable image = mContext.getResources().getDrawable(mMonument.getPhotos()[0]);
            return image;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            super.onPostExecute(drawable);
            if (mHolder.position == mPosition) {
                Assets.setFont1(mHolder.text, mContext);
                mHolder.text.setText(mMonument.getName());
                mHolder.icon.setImageDrawable(drawable);
            }
        }
    }

    static class ViewHolder {
        int position;
        TextView text;
        ImageView icon;
    }
}
