package com.elight.teaching.adapter.multiphotopicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.elight.teaching.R;
import com.elight.teaching.entity.multiphotopicker.ImageItem;
import com.elight.teaching.utils.multiphotopicker.CustomConstants;
import com.elight.teaching.utils.multiphotopicker.ImageDisplayer;

import java.util.List;

/**
 * Created by dawn on 2014/10/28.
 */
public class ImagePublishAdapter extends BaseAdapter{

    private List<ImageItem> dataList;
    private Context context;

    public ImagePublishAdapter( Context context, List<ImageItem> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        // 多返回一个用于展示添加图标
        if(dataList == null){
            return 1;
        } else if(dataList.size() == CustomConstants.MAX_IMAGE_SIZE){
            return CustomConstants.MAX_IMAGE_SIZE;
        } else {
            return dataList.size() + 1;
        }
    }

    @Override
    public Object getItem(int position) {
        if(dataList != null && dataList.size() == CustomConstants.MAX_IMAGE_SIZE){
            return dataList.get(position);
        } else if(dataList == null || position -1 < 0 || position > dataList.size()){
            return null;
        } else {
            return dataList.get(position - 1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    public View getView(int position, View convertView, ViewGroup parent) {
        //所有item展示不满一页，就不进行ViewHolder重用了，避免了一个拍照以后添加图片按钮覆盖的奇怪问题
        convertView = View.inflate(context, R.layout.multiphotopicker_image_publish,null);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.multiphotopicker_image_publish_grid_image);

        if(isShowAddItem(position)){
            imageView.setImageResource(R.drawable.multiphotopicker_add_pic);
        } else {
            final ImageItem item = dataList.get(position);
            ImageDisplayer.getInstance(context).displayBmp(imageView,item.thumbnailPath,item.sourcePath);
        }

        return convertView;
    }

    private boolean isShowAddItem(int position){
        int size = dataList == null ? 0 : dataList.size();
        return position == size;
    }
}
