package com.elight.teaching.adapter.multiphotopicker;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.elight.teaching.R;
import com.elight.teaching.entity.multiphotopicker.ImageBucket;
import com.elight.teaching.utils.multiphotopicker.ImageDisplayer;

import java.util.List;

import static com.elight.teaching.R.id.multiphotopicker_bucket_list_cover;
import static com.elight.teaching.R.id.multiphotopicker_bucket_list_title;

/**
 * Created by dawn on 2014/10/29.
 */
public class ImageBucketAdapter extends BaseAdapter{

    private Context context;
    private List<ImageBucket> dataList;

    public ImageBucketAdapter(Context context, List<ImageBucket> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.multiphotopicker_bucket_list, null);
            viewHolder = new ViewHolder();
            viewHolder.coverTV = (ImageView) convertView.findViewById(R.id.multiphotopicker_bucket_list_cover);
            viewHolder.titleTV = (TextView) convertView.findViewById(R.id.multiphotopicker_bucket_list_title);
            viewHolder.countTV = (TextView) convertView.findViewById(R.id.multiphotopicker_bucket_list_count);
            convertView.setTag(viewHolder);
        } else {
           viewHolder = (ViewHolder) convertView.getTag();
        }

        final ImageBucket item = dataList.get(position);

        if(item.imageItemList != null && item.imageItemList.size() > 0){
            String thumbnailPath = item.imageItemList.get(0).thumbnailPath;
            String sourcePath = item.imageItemList.get(0).sourcePath;
            ImageDisplayer.getInstance(context).displayBmp(viewHolder.coverTV,thumbnailPath,sourcePath);
        } else {
            viewHolder.coverTV.setImageBitmap(null);
        }
        viewHolder.titleTV.setText(item.bucketName);
        viewHolder.countTV.setText(item.count+"张");

        return convertView;
    }

    static class ViewHolder{
        private ImageView coverTV;
        private TextView titleTV;
        private TextView countTV;
    }
}
