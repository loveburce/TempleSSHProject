package com.elight.teaching.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.elight.teaching.R;
import com.elight.teaching.entity.TeachClassInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawn on 2014/9/10.
 */
public class ThemeMusicAdapter extends BaseAdapter {
    Activity activity;
    LayoutInflater layoutInflater;
    List<TeachClassInfo> lessonInfoList = new ArrayList<TeachClassInfo>();

    public ThemeMusicAdapter(Activity activity) {
        this.activity = activity;
        this.layoutInflater = LayoutInflater.from(activity);
    }

    public void addItems(List<TeachClassInfo> lessonInfoList){
            this.lessonInfoList = lessonInfoList;
    }

    @Override
    public int getCount() {
        return lessonInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return lessonInfoList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.fragment_theme_music_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.fragment_theme_music_image);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.fragment_theme_music_title);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Log.d("wangshengyanioioioioio", "referInfoList : " + lessonInfoList.get(position).toString());

        viewHolder.textView.setText(lessonInfoList.get(position).getTitle());
        ImageLoader.getInstance().displayImage(lessonInfoList.get(position).getThumbPic(), viewHolder.imageView);
//        viewHolder.imageView.setImageResource(R.drawable.head);


        return convertView;
    }

    private static class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
