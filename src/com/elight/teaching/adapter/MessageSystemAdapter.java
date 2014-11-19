package com.elight.teaching.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.listener.UpdateListener;
import com.elight.teaching.CustomApplication;
import com.elight.teaching.R;
import com.elight.teaching.adapter.base.BaseListAdapter;
import com.elight.teaching.adapter.base.ViewHolder;
import com.elight.teaching.utils.CollectionUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by dawn on 2014/10/21.
 */
public class MessageSystemAdapter extends BaseListAdapter<BmobInvitation>{

    public MessageSystemAdapter(Context context, List<BmobInvitation> list) {
        super(context, list);
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_add_friend, null);
        }
        final BmobInvitation msg = getList().get(position);
        TextView name = ViewHolder.get(convertView, R.id.name);
        ImageView iv_avatar = ViewHolder.get(convertView,R.id.avatar);
        final Button btn_add = ViewHolder.get(convertView,R.id.btn_add);

        String avatar = msg.getAvatar();

        if(avatar != null && !avatar.equals("")){
            ImageLoader.getInstance().displayImage(avatar,iv_avatar);
        } else{
            iv_avatar.setImageResource(R.drawable.default_head);
        }

        int status = msg.getStatus();

        if(status == BmobConfig.INVITE_ADD_NO_VALIDATION){
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    agressAdd(btn_add, msg);
                }
            });
        } else if(status == BmobConfig.INVITE_ADD_AGREE){
            btn_add.setText("已同意");
            btn_add.setBackgroundDrawable(null);
            btn_add.setTextColor(mContext.getResources().getColor(R.color.base_color_text_black));
            btn_add.setEnabled(false);
        }

        name.setText(msg.getFromname());

        return convertView;
    }

    private void agressAdd(final Button btn_add, final BmobInvitation msg){
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("正在添加。。。");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        try {
            //同意添加好友
            BmobUserManager.getInstance(mContext).agreeAddContact(msg, new UpdateListener() {
                @Override
                public void onSuccess() {
                    progressDialog.dismiss();
                    btn_add.setText("已同意");
                    btn_add.setBackgroundDrawable(null);
                    btn_add.setTextColor(mContext.getResources().getColor(R.color.base_color_text_black));
                    btn_add.setEnabled(false);
                    CustomApplication.getInstance().setContactList(CollectionUtils.list2map(BmobDB.create(mContext).getContactList()));
                }

                @Override
                public void onFailure(int i, String s) {
                    progressDialog.dismiss();
                }
            });
        }catch (final Exception e){
            progressDialog.dismiss();
        }
    }

}
