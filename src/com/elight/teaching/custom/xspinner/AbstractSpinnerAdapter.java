package com.elight.teaching.custom.xspinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.elight.teaching.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawn on 2014/9/26.
 */
public class AbstractSpinnerAdapter<T> extends BaseAdapter {

    private Context mContext;
    private List<T> mObjects = new ArrayList<T>();
    private int mSelectItem = 0;
    private LayoutInflater mInflater;

    public AbstractSpinnerAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refreshData(List<T> objects, int selIndex){
        mObjects = objects;
        if(selIndex < 0){
            selIndex = 0;
        }
        if(selIndex >= mObjects.size()){
            selIndex = mObjects.size() - 1;
        }
        mSelectItem = selIndex;
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public Object getItem(int position) {
        return mObjects.get(position).toString();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.spinner_item_layout,null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Object item = getItem(position);
        viewHolder.mTextView.setText(item.toString());

        return convertView;
    }

    public static class ViewHolder{
        public TextView mTextView;
    }

    public static interface IOnItemSelectListener{
        public void onItemClick(int pos);
    };
}
