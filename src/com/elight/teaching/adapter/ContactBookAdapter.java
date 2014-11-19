package com.elight.teaching.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import com.elight.teaching.R;
import com.elight.teaching.entity.UserInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by dawn on 2014/9/29.
 */
public class ContactBookAdapter extends BaseAdapter implements SectionIndexer{

    private Context context;
    private List<UserInfo> data;

    public ContactBookAdapter(Context context, List<UserInfo> data) {
        this.context = context;
        this.data = data;
    }

    public void updateListView(List<UserInfo> list){
        this.data = list;
        notifyDataSetChanged();
    }

    public void remove(UserInfo userInfo){
        this.data.remove(userInfo);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user_friend, null);
            viewHolder = new ViewHolder();
            viewHolder.alpha = (TextView) convertView.findViewById(R.id.alpha);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_friend_name);
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.img_friend_avatar);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        UserInfo friend = data.get(position);
        final String name = friend.getUsername();
        final String avatar = friend.getAvatar();

        if(!TextUtils.isEmpty(avatar)){
            ImageLoader.getInstance().displayImage(avatar, viewHolder.avatar);
        } else{
            viewHolder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.head));
        }
        viewHolder.name.setText(name);

        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.alpha.setVisibility(View.VISIBLE);
            viewHolder.alpha.setText(friend.getSortLetters());
        } else {
            viewHolder.alpha.setVisibility(View.GONE);
        }

        return convertView;
    }

    /*首字母提示*/
    static class ViewHolder{
        TextView alpha;
        ImageView avatar;
        TextView name;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = data.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section){
                return i;
            }
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return data.get(position).getSortLetters().charAt(0);
    }
}
