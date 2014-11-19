package com.elight.teaching.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.db.BmobDB;
import com.elight.teaching.R;
import com.elight.teaching.adapter.MessageSystemAdapter;
import com.elight.teaching.custom.dialog.DialogTips;

/**
 * Created by dawn on 2014/9/5.
 */
public class MessagesSystemFragment extends BaseFragment implements AdapterView.OnItemLongClickListener{

    public static BaseFragment newInstance(int index) {
        BaseFragment fragment = new MessagesSystemFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        fragment.setIndex(index);
        return fragment;
    }

    private View layoutView;
    ListView listView;
    MessageSystemAdapter messageSystemAdapter;
    String from = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        from = getActivity().getIntent().getStringExtra("from");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_message_system,null);
        initView();
        return layoutView;
    }

    private void initView(){
        listView = (ListView) layoutView.findViewById(R.id.list);
        listView.setOnItemLongClickListener(this);
        messageSystemAdapter = new MessageSystemAdapter(getActivity(), BmobDB.create(getActivity()).queryBmobInviteList());
        listView.setAdapter(messageSystemAdapter);
        if(from == null){   //若来自通知栏的点击，则定位到最后一条
            listView.setSelection(messageSystemAdapter.getCount());
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        BmobInvitation invitation = (BmobInvitation) messageSystemAdapter.getItem(position);
        showDeleteDialog(position, invitation);
        return true;
    }

    public void showDeleteDialog(final int position, final BmobInvitation invitation){
        DialogTips dialogTips = new DialogTips(getActivity(),invitation.getFromname(),"删除好友请求","确定",true,true);
         dialogTips.show();
        dialogTips = null;
    }

    private void deleteInvite(int position, BmobInvitation invitation){
        messageSystemAdapter.remove(position);
        BmobDB.create(getActivity()).deleteInviteMsg(invitation.getFromid(), Long.toString(invitation.getTime()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
