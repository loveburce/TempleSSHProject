package com.elight.teaching.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobRecent;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.listener.UpdateListener;
import com.elight.teaching.CustomApplication;
import com.elight.teaching.R;
import com.elight.teaching.activity.ChatActivity;
import com.elight.teaching.adapter.ContactBookAdapter;
import com.elight.teaching.custom.ClearEditText;
import com.elight.teaching.custom.MyLetterView;
import com.elight.teaching.custom.dialog.DialogTips;
import com.elight.teaching.entity.UserInfo;
import com.elight.teaching.utils.ActivityUtils;
import com.elight.teaching.utils.CharacterParser;
import com.elight.teaching.utils.CollectionUtils;
import com.elight.teaching.utils.PinyinComparator;

import java.util.*;

/**
 * Created by dawn on 2014/9/3.
 */
@SuppressLint("DefaultLocale")
public class ContactBookFragment extends BaseFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static BaseFragment newInstance(int index) {
        BaseFragment fragment = new ContactBookFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        fragment.setIndex(index);
        return fragment;
    }

    private View layoutView;
    ClearEditText clearEditText;
    TextView dialog;
    ListView list_friends;
    MyLetterView right_letter;
    private ContactBookAdapter userFriendAdapter;    //好友
    List<UserInfo> friends = new ArrayList<UserInfo>();
    private InputMethodManager inputMethodManager;
    /*汉子转化成拼音的类*/
    private CharacterParser characterParser;
    /*根据拼音来排列ListView里面的数据*/
    private PinyinComparator pinyinComparator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_contact_book, container, false);
        return layoutView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        init();
    }

    private void init(){
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        initListView();
        initRightLetterView();
        initEditText();
    }

    private void initEditText(){
        clearEditText = (ClearEditText)findViewById(R.id.et_msg_search);
        clearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /*根据输入框中的值来过滤数据并更新ListView*/
    private void filterData(String filterStr){
        List<UserInfo> filterDateList = new ArrayList<UserInfo>();
        if(TextUtils.isEmpty(filterStr)){
            filterDateList = friends;
        } else {
            filterDateList.clear();
            for(UserInfo sortModel : friends){
                String name = sortModel.getUsername();
                if(name != null){
                    if (name.indexOf(filterStr.toString()) != -1|| characterParser.getSelling(name).startsWith(filterStr.toString())) {
                        filterDateList.add(sortModel);
                    }
                }
            }
        }
        Collections.sort(filterDateList, pinyinComparator);
        userFriendAdapter.updateListView(filterDateList);
    }

    private void filledData(List<BmobChatUser> datas){
        friends.clear();
        int total = datas.size();
        for(int i=0; i<total; i++){
            BmobChatUser user = datas.get(i);
            UserInfo sortModel = new UserInfo();
            sortModel.setAvatar(user.getAvatar());
            sortModel.setNick(user.getNick());
            sortModel.setUsername(user.getUsername());
            sortModel.setObjectId(user.getObjectId());
            sortModel.setContacts(user.getContacts());
            //汉字转换成拼音
            String username = sortModel.getUsername();
            //若没有username
            if(username != null){
                String pinyin = characterParser.getSelling(sortModel.getUsername());
                String sortString = pinyin.substring(0, 1).toUpperCase();
                //正则表达式，判断首字母是否是英文字母
                if(sortString.matches("[A-Z]")){
                    sortModel.setSortLetters(sortString.toUpperCase());
                } else {
                    sortModel.setSortLetters("#");
                }
            } else {
                sortModel.setSortLetters("#");
            }
            friends.add(sortModel);
        }
        Collections.sort(friends, pinyinComparator);
    }

    private void initListView(){
        list_friends= (ListView)findViewById(R.id.list_friends);
        userFriendAdapter = new ContactBookAdapter(getActivity(),friends);
        list_friends.setAdapter(userFriendAdapter);
        list_friends.setOnItemClickListener(this);
        list_friends.setOnItemLongClickListener(this);

        list_friends.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //隐藏软键盘
                if(getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN){
                    if (getActivity().getCurrentFocus() != null)
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser){
            queryMyfriends();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void initRightLetterView(){
        right_letter = (MyLetterView) findViewById(R.id.right_letter);
        dialog = (TextView) findViewById(R.id.dialog);
        right_letter.setTextView(dialog);
        right_letter.setOnTouchingLetterChangedListener(new LetterListViewListener());
    }

    private class LetterListViewListener implements MyLetterView.OnTouchingLetterChangedListener{
        @Override
        public void onTouchingLetterChanged(String s) {
            int position = userFriendAdapter.getPositionForSection(s.charAt(0));
            if(position != -1){
                list_friends.setSelection(position);
            }
        }
    }

    /*获取好友列表*/
    private void queryMyfriends(){
        //是否有新好友请求
        if(BmobDB.create(getActivity()).hasNewInvite()){
            //新好友请求时的通知
        }

        Map<String, BmobChatUser> userMap = CustomApplication.getInstance().getContactList();
        //组装新的User
        filledData(CollectionUtils.map2list(userMap));
        if(userFriendAdapter == null){
            userFriendAdapter = new ContactBookAdapter(getActivity(), friends);
        } else {
            userFriendAdapter.notifyDataSetChanged();
        }
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

    @Override
    public void onResume() {
        super.onResume();
        if(!hidden){
            refresh();
        }
    }

    public void refresh(){
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    queryMyfriends();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //进入好友的详细资料页面
        UserInfo userInfo = (UserInfo) userFriendAdapter.getItem(position);
        //重置未读消息
//        BmobDB.create(getActivity()).resetUnread(userInfo.getTargetid());
        //组装聊天对象
        BmobChatUser bmobChatUser = new BmobChatUser();
        bmobChatUser.setAvatar(userInfo.getAvatar());
        bmobChatUser.setNick(userInfo.getNick());
        bmobChatUser.setUsername(userInfo.getUsername());
        bmobChatUser.setObjectId(userInfo.getObjectId());

        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("user",bmobChatUser);
        startAnimActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        UserInfo userInfo = (UserInfo) userFriendAdapter.getItem(position - 1);
        showDeleteDialog(userInfo);
        return true;
    }

    public void showDeleteDialog(final UserInfo userInfo){
        DialogTips dialog = new DialogTips(getActivity(),userInfo.getUsername(),"删除联系人", "确定",true,true);
        // 设置成功事件
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                deleteContact(userInfo);
            }
        });
        // 显示确认对话框
        dialog.show();
        dialog = null;
    }

    /*删除联系人*/
    private void deleteContact(final UserInfo user){
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("正在删除...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        bmobUserManager.deleteContact(user.getObjectId(), new UpdateListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                ActivityUtils.showShortToast(getActivity(), "删除成功");
                //删除内存
                CustomApplication.getInstance().getContactList().remove(user.getUsername());
                //更新界面
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        progress.dismiss();
                        userFriendAdapter.remove(user);
                    }
                });
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
                ActivityUtils.showShortToast(getActivity(),"删除失败："+arg1);
                progress.dismiss();
            }
        });
    }
}
