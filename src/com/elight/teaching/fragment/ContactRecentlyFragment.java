package com.elight.teaching.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobRecent;
import cn.bmob.im.db.BmobDB;
import com.elight.teaching.R;
import com.elight.teaching.activity.ChatActivity;
import com.elight.teaching.adapter.ContactRecentAdapter;
import com.elight.teaching.custom.ClearEditText;
import com.elight.teaching.custom.dialog.DialogTips;
import com.lidroid.xutils.view.annotation.event.OnItemLongClick;

/**
 * Created by dawn on 2014/9/3.
 */
public class ContactRecentlyFragment extends BaseFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    ClearEditText clearEditText;
    ListView listView;
    private View layoutView;
    ContactRecentAdapter contactRecentAdapter;

    public static BaseFragment newInstance(int index) {
        BaseFragment fragment = new ContactRecentlyFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        fragment.setIndex(index);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_contact_recently,container,false);
        return layoutView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!hidden){
            refresh();
        }
    }

    private void initView(){
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        contactRecentAdapter = new ContactRecentAdapter(getActivity(), R.layout.item_conversation, BmobDB.create(getActivity()).queryRecents());
        listView.setAdapter(contactRecentAdapter);

        clearEditText = (ClearEditText) findViewById(R.id.et_msg_search);
        clearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                contactRecentAdapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /*删除会话*/
    private void deleteRecent(BmobRecent recent){
        contactRecentAdapter.remove(recent);
        BmobDB.create(getActivity()).deleteRecent(recent.getTargetid());
        BmobDB.create(getActivity()).deleteMessages(recent.getTargetid());
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        BmobRecent recent = contactRecentAdapter.getItem(position);
        showDeleteDialog(recent);
        return false;
    }

    public void showDeleteDialog(final BmobRecent recent){
        DialogTips dialogTips = new DialogTips(getActivity(),recent.getUserName(),"删除会话", "确定",true,true);
        //设置成功事件
        dialogTips.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteRecent(recent);
            }
        });
        //显示确认对话框
        dialogTips.show();
        dialogTips = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BmobRecent recent = contactRecentAdapter.getItem(position);
        //重置未读消息
        BmobDB.create(getActivity()).resetUnread(recent.getTargetid());
        //组装聊天对象
        BmobChatUser bmobChatUser = new BmobChatUser();
        bmobChatUser.setAvatar(recent.getAvatar());
        bmobChatUser.setNick(recent.getNick());
        bmobChatUser.setUsername(recent.getUserName());
        bmobChatUser.setObjectId(recent.getTargetid());

        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("user",bmobChatUser);
        startAnimActivity(intent);
    }

    private boolean hidden;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if(!hidden){
            refresh();
        }
    }

    public void refresh(){
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    contactRecentAdapter = new ContactRecentAdapter(getActivity(), R.layout.item_conversation, BmobDB.create(getActivity()).queryRecents());
                    listView.setAdapter(contactRecentAdapter);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
