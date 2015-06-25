package madelyntav.c4q.nyc.googlecards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by alvin2 on 6/24/15.
 */

public class ImageAdapter extends BaseAdapter {

    Context mContext;
    List<String> imageUrlList;

    public ImageAdapter(Context mContext, List<String> imageUrlList) {
        this.mContext = mContext;
        this.imageUrlList = imageUrlList;

    }

    @Override
    public int getCount() {
        return imageUrlList.size();
    }

    @Override
    public String getItem(int position) {
        return imageUrlList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return imageUrlList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = new ImageView(mContext);
        }


        String imageUrl = getItem(position);
        Picasso.with(mContext).load(imageUrl).resize(300, 300).centerCrop().into((ImageView) convertView);

        return convertView;
    }
}
