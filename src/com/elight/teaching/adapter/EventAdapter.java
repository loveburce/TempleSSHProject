package com.elight.teaching.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.listener.UpdateListener;
import com.elight.teaching.CustomApplication;
import com.elight.teaching.R;
import com.elight.teaching.activity.UserChooseActivity;
import com.elight.teaching.activity.UserLoginActivity;
import com.elight.teaching.adapter.base.BaseListAdapter;
import com.elight.teaching.adapter.base.ViewHolder;
import com.elight.teaching.entity.EventInfo;
import com.elight.teaching.entity.UserInfo;
import com.elight.teaching.utils.ActivityUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

/**
 * Created by dawn on 2014/10/15.
 */
public class EventAdapter extends BaseListAdapter<EventInfo>{

    public EventAdapter(Context context, List<EventInfo> list) {
        super(context, list);
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.fragment_event_item , null);
        }
        final EventInfo eventInfo = getList().get(position);
        UserInfo userInfo = eventInfo.getAuthor();
        //ID 注册
        final ImageView userLogo = ViewHolder.get(convertView,R.id.fragment_event_item_user_avatar);
        TextView userName = ViewHolder.get(convertView,R.id.fragment_event_item_user_name);
        TextView contentText = ViewHolder.get(convertView,R.id.fragment_event_item_user_content_text);
        final ImageView contentImage = ViewHolder.get(convertView,R.id.fragment_event_item_user_content_image);

        final ImageView favMark = ViewHolder.get(convertView,R.id.fragment_event_item_user_fav);
        final TextView love = ViewHolder.get(convertView,R.id.fragment_event_item_user_love);
        final TextView hate = ViewHolder.get(convertView,R.id.fragment_event_item_user_hate);
        TextView share = ViewHolder.get(convertView,R.id.fragment_event_item_user_share);
        final TextView comment = ViewHolder.get(convertView,R.id.fragment_event_item_user_comment);

        // 控件内容填写
        String avatarUrl = null;
        if(null != userInfo){
            avatarUrl = userInfo.getAvatar();
        }
        ImageLoader.getInstance().displayImage(avatarUrl, userLogo,CustomApplication.getInstance().getOptions(),
                new SimpleImageLoadingListener(){
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                    }
                });

        userLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CustomApplication.getInstance().getCurrentUser() == null){
                    ActivityUtils.showShortToast(mContext,"请先登录。。。");
                    Intent intent = new Intent();
                    intent.setClass(mContext, UserLoginActivity.class);
                    CustomApplication.getInstance().getTopActivity().startActivity(intent);
                    return;
                }
                CustomApplication.getInstance().setCurrentEventInfo(eventInfo);
                //否则进入个人信息界面

            }
        });

//        userName.setText(eventInfo.getAuthor().getUsername());
//        contentText.setText(eventInfo.getContent());


//        if(null == eventInfo.getContentFigureUrl()){
//            contentImage.setVisibility(View.GONE);
//        } else {
//            contentImage.setVisibility(View.VISIBLE);
//            ImageLoader.getInstance().displayImage(eventInfo.getContentFigureUrl().getFileUrl()==null?"":eventInfo.getContentFigureUrl().getFileUrl(),
//                    userLogo,
//                    CustomApplication.getInstance().getOptions(R.drawable.loading_animation),
//                    new SimpleImageLoadingListener(){
//                        @Override
//                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                            super.onLoadingComplete(imageUri, view, loadedImage);
//                            float[] cons = ActivityUtils.getBitmapConfiguration(loadedImage, contentImage, 1.0f);
//                            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int)cons[0], (int)cons[1]);
//                            layoutParams.addRule(RelativeLayout.BELOW,R.id.fragment_event_item_user_content_text);
//                            contentImage.setLayoutParams(layoutParams);
//                        }
//                    });
//        }

        love.setText(eventInfo.getLove()+"");
        if(eventInfo.isMyLove()){
            love.setTextColor(Color.parseColor("#D95555"));
        } else {
            love.setTextColor(Color.parseColor("#000000"));
        }

        hate.setText(eventInfo.getHate() + "");
        love.setOnClickListener(new View.OnClickListener() {
            boolean oldFav = eventInfo.isMyFav();

            @Override
            public void onClick(View v) {
                if(CustomApplication.getInstance().getCurrentUser() == null){
                    ActivityUtils.showShortToast(mContext,"请先登录。。。");
                    Intent intent = new Intent();
                    intent.setClass(mContext, UserChooseActivity.class);
                    CustomApplication.getInstance().getTopActivity().startActivity(intent);
                    return;
                }
                if(eventInfo.isMyFav()){
                    ActivityUtils.showShortToast(mContext,"您已经赞过了。。。");
                    return;
                }
                //您是否已经赞过
//                if(DatabaseUtils.get){
//
//                }

                //
                eventInfo.setLove(eventInfo.getLove()+1);
                love.setTextColor(Color.parseColor("#D95555"));
                love.setText(eventInfo.getLove()+"");

                eventInfo.increment("love",1);
                if(eventInfo.isMyFav()){
                    eventInfo.setMyFav(false);
                }

                eventInfo.update(mContext, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        eventInfo.setMyLove(true);
                        eventInfo.setMyFav(oldFav);
//                        DatabaseUtils.getInstance
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        eventInfo.setMyLove(true);
                        eventInfo.setMyFav(oldFav);
                    }
                });
            }
        });

        hate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventInfo.setHate(eventInfo.getHate()+1);
                hate.setText(eventInfo.getHate()+"");
                eventInfo.increment("hate",1);
                eventInfo.update(mContext, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        ActivityUtils.showShortToast(mContext, "点赞成功~");
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.showShortToast(mContext,"分享给好友~");
                //  分享代码
            }
        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CustomApplication.getInstance().getCurrentUser() == null){
                    ActivityUtils.showShortToast(mContext,"请先登录");
                    Intent intent = new Intent();
                    intent.setClass(mContext, UserChooseActivity.class);
                    CustomApplication.getInstance().getTopActivity().startActivity(intent);
                    return;
                }
//                Intent intent = new Intent();
//                intent.setClass(CustomApplication.getInstance().getTopActivity(), );
                //评论的模块
            }
        });

        if(eventInfo.isMyFav()){
            favMark.setImageResource(R.drawable.ic_action_fav_choose);
        }else{
            favMark.setImageResource(R.drawable.ic_action_fav_normal);
        }

        favMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.showShortToast(mContext,"收藏");
//                onClick(v, eventInfo);
            }
        });

        return convertView;
    }
}
