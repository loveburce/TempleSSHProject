package com.elight.teaching.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cn.bmob.im.bean.BmobRecent;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;
import com.elight.teaching.R;
import com.elight.teaching.adapter.base.ViewHolder;
import com.elight.teaching.utils.FaceTextUtils;
import com.elight.teaching.utils.TimeUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawn on 2014/9/30.
 */
public class ContactRecentAdapter extends ArrayAdapter<BmobRecent> implements Filterable{

    private LayoutInflater layoutInflater;
    private List<BmobRecent> mData;
    private Context mContext;

    public ContactRecentAdapter(Context context, int textViewResourceId, List<BmobRecent> objects){
        super(context, textViewResourceId, objects);
        layoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        mData = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final BmobRecent item = mData.get(position);
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_conversation, parent, false);
        }
        ImageView iv_recent_avatar = ViewHolder.get(convertView, R.id.iv_recent_avatar);
        TextView tv_recent_name = ViewHolder.get(convertView, R.id.tv_recent_time);
        TextView tv_recent_msg = ViewHolder.get(convertView, R.id.tv_recent_msg);
        TextView tv_recent_time = ViewHolder.get(convertView, R.id.tv_recent_time);
        TextView tv_recent_unread = ViewHolder.get(convertView,R.id.tv_recent_unread);

        //填充数据
        String avatar = item.getAvatar();
        if(avatar != null && !avatar.equals("")){
            ImageLoader.getInstance().displayImage(avatar,iv_recent_avatar);
        } else {
            iv_recent_avatar.setImageResource(R.drawable.head);
        }

        tv_recent_name.setText(item.getUserName());
        tv_recent_time.setText(TimeUtils.getChatTime(item.getTime()));
        int num = BmobDB.create(mContext).getUnreadCount(item.getTargetid());
        //显示内容
        if(item.getType() == BmobConfig.TYPE_TEXT){
            SpannableString spannableString = FaceTextUtils.toSpannableString(mContext, item.getMessage());
            tv_recent_msg.setText(spannableString);
        } else if (item.getType() == BmobConfig.TYPE_IMAGE){
            tv_recent_msg.setText("[图片]");
        } else if (item.getType() == BmobConfig.TYPE_LOCATION){
            String all = item.getMessage();
            //位置类型的信息组装格式，地理位置&纬度&经度
            if(all != null && !all.equals("")){
                String address = all.split("&")[0];
                tv_recent_msg.setText("[位置]"+address);
            }
        } else if (item.getType() == BmobConfig.TYPE_VOICE){
            tv_recent_msg.setText("[语音]");
        }

        if(num > 0){
            tv_recent_unread.setVisibility(View.VISIBLE);
            tv_recent_unread.setText(num + "");
        } else {
            tv_recent_unread.setVisibility(View.GONE);
        }

        return convertView;
    }
}
