package com.elight.teaching.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.elight.teaching.adapter.MessagePrivateAdapter;
import com.elight.teaching.custom.ClearEditText;
import com.elight.teaching.custom.dialog.DialogTips;

/**
 * Created by dawn on 2014/9/5.
 */
public class MessagesPrivateFragment extends BaseFragment implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

    public static BaseFragment newInstance(int index) {
        BaseFragment fragment = new MessagesPrivateFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        fragment.setIndex(index);
        return fragment;
    }

    private View layoutView;
    private ClearEditText clearEditText;
    private ListView listView;
    private MessagePrivateAdapter messagePrivateAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_message_private,null);
        return layoutView;
    }

    private void initView(){
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        messagePrivateAdapter = new MessagePrivateAdapter(getActivity(),R.layout.item_conversation, BmobDB.create(getActivity()).queryRecents());
        listView.setAdapter(messagePrivateAdapter);
        clearEditText = (ClearEditText) findViewById(R.id.fragment_message_private_clear_et);
        clearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                messagePrivateAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void deleteRecent(BmobRecent bmobRecent){
        messagePrivateAdapter.remove(bmobRecent);
        BmobDB.create(getActivity()).deleteRecent(bmobRecent.getTargetid());
        BmobDB.create(getActivity()).deleteMessages(bmobRecent.getTargetid());
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        BmobRecent recent = messagePrivateAdapter.getItem(position);
        showDeleteDialog(recent);
        return true;
    }

    public void showDeleteDialog(final BmobRecent recent){
        DialogTips dialogTips = new DialogTips(getActivity(),recent.getUserName(),"删除会话","确定",true,true);
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
        BmobRecent bmobRecent = messagePrivateAdapter.getItem(position);
        //重置未读消息
        BmobDB.create(getActivity()).resetUnread(bmobRecent.getTargetid());
        //组装聊天对象
        BmobChatUser chatUser = new BmobChatUser();
        chatUser.setAvatar(bmobRecent.getAvatar());
        chatUser.setNick(bmobRecent.getNick());
        chatUser.setUsername(bmobRecent.getUserName());
        chatUser.setObjectId(bmobRecent.getTargetid());
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("user",chatUser);
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
                    messagePrivateAdapter = new MessagePrivateAdapter(getActivity(),R.layout.item_conversation,BmobDB.create(getActivity()).queryRecents());
                    listView.setAdapter(messagePrivateAdapter);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!hidden){
            refresh();
        }
    }
}
