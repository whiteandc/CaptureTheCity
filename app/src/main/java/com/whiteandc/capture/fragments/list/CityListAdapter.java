package com.whiteandc.capture.fragments.list;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whiteandc.capture.Assets;
import com.whiteandc.capture.R;
import com.whiteandc.capture.data.Monument;
import com.whiteandc.capture.data.MonumentList;

import java.util.ArrayList;
import java.util.List;

/**
 *  Para Blanca: http://developer.android.com/training/improving-layouts/smooth-scrolling.html
 *  Aqui explica para que se usa el viewHolder y el Asynctask
 *  http://developer.android.com/reference/android/os/AsyncTask.html
 */
public class CityListAdapter extends BaseAdapter {
	private final Activity context;

    private LayoutInflater inflater;
    private List<Monument> monumentList= MonumentList.getList();

    public CityListAdapter(Activity context) {
        //super(context, R.layout.city_list, monumentList);
        this.context = context;
        this.inflater = context.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return monumentList.size();
    }

    @Override
    public Object getItem(int position) {
        return monumentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
        new ThumbnailTask(position, holder, context, monumentList)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
		return view;
	}

    public void searchMonument(String query) {
        monumentList= MonumentList.getList();
        List<Monument> list= new ArrayList<>(monumentList);
        for (Monument m : list) {
            if (!m.getName().toUpperCase().contains(query.toUpperCase())) {
                monumentList.remove(m);
            }
        }
        notifyDataSetChanged();
    }

    private class ThumbnailTask extends AsyncTask<ViewHolder, Void, Drawable> {
        private int mPosition;
        private ViewHolder mHolder;
        private Activity mContext;
        private Monument mMonument;
        private List<Monument> mMonuments;

        public ThumbnailTask(int position, ViewHolder holder, Activity context, List<Monument> monuments) {
            mPosition = position;
            mHolder = holder;
            mContext = context;
            mMonuments= monuments;
        }

        @Override
        //We should download the pictures here
        protected Drawable doInBackground(ViewHolder... params) {
            Drawable image = null;
            mMonument = mMonuments.get(mPosition);
            if(mMonument.getMainPicture() == null) {
                image = mContext.getResources().getDrawable(mMonument.getPhotos()[0]);
                mMonument.setMainPicture(image);
            }else{
                image = mMonument.getMainPicture();
            }
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
