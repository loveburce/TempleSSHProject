package com.elight.teaching.custom.xgrid;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.elight.teaching.R;
import com.lidroid.xutils.http.client.multipart.content.StringBody;

import java.util.List;

/**
 * Created by dawn on 2014/10/22.
 */
public class DateAdapter extends BaseAdapter{

    private Context context;
    private List<String> lstDate;
    private TextView txtAge;

    public DateAdapter(Context mContext, List<String> list) {
        this.context = mContext;
        lstDate = list;
    }

    @Override
    public int getCount() {
        return lstDate.size();
    }

    @Override
    public Object getItem(int position) {
        return lstDate.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void exchange(int startPosition, int endPosition) {
        Object endObject = getItem(endPosition);
        Object startObject = getItem(startPosition);
        lstDate.add(startPosition, (String) endObject);
        lstDate.remove(startPosition + 1);
        lstDate.add(endPosition, (String) startObject);
        lstDate.remove(endPosition + 1);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        convertView = LayoutInflater.from(context).inflate(R.layout.fragment_main_item, null);
//        txtAge = (TextView) convertView.findViewById(R.id.fragment_main_item_title);
//        txtAge.setText("" + lstDate.get(position));
        return convertView;
    }


}
