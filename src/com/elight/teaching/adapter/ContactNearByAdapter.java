package com.elight.teaching.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.datatype.BmobGeoPoint;
import com.elight.teaching.CustomApplication;
import com.elight.teaching.R;
import com.elight.teaching.adapter.base.BaseListAdapter;
import com.elight.teaching.adapter.base.ViewHolder;
import com.elight.teaching.entity.UserInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.security.PublicKey;
import java.util.List;

/**
 * Created by dawn on 2014/9/30.
 */
public class ContactNearByAdapter extends BaseListAdapter<UserInfo>{

    public ContactNearByAdapter(Context context, List<UserInfo> list) {
        super(context, list);
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_near_people, null);
        }
        final UserInfo contract = getList().get(position);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        TextView tv_distance = ViewHolder.get(convertView, R.id.tv_distance);
        TextView tv_logintime = ViewHolder.get(convertView, R.id.tv_logintime);
        ImageView iv_avatar = ViewHolder.get(convertView, R.id.iv_avatar);

        String avatar = contract.getAvatar();
        if(avatar != null && !avatar.equals("")){
            ImageLoader.getInstance().displayImage(avatar,iv_avatar);
        } else{
            iv_avatar.setImageResource(R.drawable.default_head);
        }

        BmobGeoPoint location = contract.getLocation();
        String currentLat = CustomApplication.getInstance().getLatitude();
        String currentLong = CustomApplication.getInstance().getLongitude();
        if(location != null && !currentLat.equals("") && !currentLong.equals("")){
            double distance = DistanceOfTwoPoints(Double.parseDouble(currentLat),Double.parseDouble(currentLong),contract.getLocation().getLatitude(),contract.getLocation().getLongitude());
            tv_distance.setText(String.valueOf(distance +"米"));
        } else {
            tv_distance.setText("未知");
        }
        tv_name.setText(contract.getUsername());
        tv_logintime.setText("最近登录时间:"+contract.getUpdatedAt());

        return convertView;
    }

    private static final double EARTH_RADIUS = 6378137;

    private static double rad(double d){
        return d * Math.PI / 180.0;
    }

    public static double DistanceOfTwoPoints(double lat1, double lng1, double lat2, double lng2){
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);

        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a /2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b /2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
     }
}
