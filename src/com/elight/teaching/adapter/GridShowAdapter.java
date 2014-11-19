package com.elight.teaching.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.elight.teaching.R;
import com.elight.teaching.entity.IndexGalleryItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawn on 2014/9/4.
 */
public class GridShowAdapter extends BaseAdapter {
    Activity activity;
    LayoutInflater layoutInflater;
    List<IndexGalleryItemData> dataArrayList = new ArrayList<IndexGalleryItemData>();

    public GridShowAdapter(Activity activity) {
        this.activity = activity;
        this.layoutInflater = LayoutInflater.from(activity);
    }

    public void addItems(ArrayList<IndexGalleryItemData> dataArrayList){
        if(dataArrayList != null){
            this.dataArrayList = dataArrayList;
        }
    }

    @Override
    public int getCount() {
        return dataArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.fragment_grid_show_item,null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.grid_view_item_image);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.grid_view_item_text);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //下载图片工具
        viewHolder.imageView.setImageResource(dataArrayList.get(position).getImageUrl());
        viewHolder.textView.setText(dataArrayList.get(position).getText());
        return convertView;
    }

    private static class ViewHolder{
        ImageView imageView;
        TextView textView;
    }

}
