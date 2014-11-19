package com.elight.teaching.adapter.multiphotopicker;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.elight.teaching.R;
import com.elight.teaching.entity.multiphotopicker.ImageItem;
import com.elight.teaching.utils.multiphotopicker.ImageDisplayer;

import java.util.List;

/**
 * Created by dawn on 2014/10/29.
 */
public class ImageGridAdapter extends BaseAdapter{

    private Context context;
    private List<ImageItem> dataList;

    public ImageGridAdapter(Context context, List<ImageItem> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
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
            convertView = View.inflate(context, R.layout.multiphotopicker_image_list,null);
            viewHolder = new ViewHolder();
            viewHolder.imageIv = (ImageView) convertView.findViewById(R.id.multiphotopicker_image_list_image);
            viewHolder.selectedIv = (ImageView) convertView.findViewById(R.id.multiphotopicker_image_list_selected_tag);
            viewHolder.selectedBgTv = (TextView) convertView.findViewById(R.id.multiphotopicker_image_list_selected_bg);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ImageItem item = dataList.get(position);

        ImageDisplayer.getInstance(context).displayBmp(viewHolder.imageIv, item.thumbnailPath,item.sourcePath);

        if(item.isSelected){
            viewHolder.selectedIv.setImageDrawable(context.getResources().getDrawable(R.drawable.tag_selected));
            viewHolder.selectedIv.setVisibility(View.VISIBLE);
            viewHolder.selectedBgTv.setBackgroundResource(R.drawable.multiphotopicker_image_selected);
        } else {
            viewHolder.selectedIv.setImageDrawable(null);
            viewHolder.selectedIv.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder{
        private ImageView imageIv;
        private ImageView selectedIv;
        private TextView selectedBgTv;
    }
}
