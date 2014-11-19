package com.elight.teaching.activity;

import android.app.ProgressDialog;
import android.content.*;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.listener.SaveListener;
import com.elight.teaching.R;
import com.elight.teaching.config.Constants;
import com.elight.teaching.custom.dialog.DialogTips;
import com.elight.teaching.custom.xspinner.AbstractSpinnerAdapter;
import com.elight.teaching.custom.xspinner.CustomObject;
import com.elight.teaching.custom.xspinner.CustomSpinnerAdapter;
import com.elight.teaching.custom.xspinner.SpinnerPopWindow;
import com.elight.teaching.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawn on 2014/9/2.
 */
public class UserLoginActivity extends BaseActivity implements View.OnClickListener, AbstractSpinnerAdapter.IOnItemSelectListener{

    private View rootView;
    private TextView textViewType;
    private ImageButton imageButtonDropDown;
    private List<CustomObject> typeList = new ArrayList<CustomObject>();
    private AbstractSpinnerAdapter spinnerAdapter;
    private SpinnerPopWindow spinnerPopWindow;

    EditText editTextName;
    EditText editTextPassword;
    Button buttonLogin;
    BmobChatUser currentUser;
    private MyBroadcastReceiver broadcastReceiver = new MyBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        initView();
        findViewById();

        //注册退出广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_REGISTER_SUCCESS_FINISH);
        registerReceiver(broadcastReceiver, intentFilter);

    }

    @Override
    protected void findViewById() {
        rootView = findViewById(R.id.activity_user_login_root_view);
        textViewType = (TextView) findViewById(R.id.activity_user_login_value);
        imageButtonDropDown = (ImageButton) findViewById(R.id.activity_user_login_dropdown);
        imageButtonDropDown.setOnClickListener(this);

        String[] types = getResources().getStringArray(R.array.user_type_name);
        for(int i=0; i<types.length;i++){
            CustomObject object = new CustomObject();
            object.data = types[i];
            typeList.add(object);
        }

        spinnerAdapter = new CustomSpinnerAdapter(this);
        spinnerAdapter.refreshData(typeList, 0);

        spinnerPopWindow = new SpinnerPopWindow(UserLoginActivity.this);
        spinnerPopWindow.setAdapter(spinnerAdapter);
        spinnerPopWindow.setItemSelectListener(this);

        editTextName = (EditText) findViewById(R.id.activity_user_login_username);
        editTextPassword = (EditText) findViewById(R.id.activity_user_login_password);

        buttonLogin = (Button) findViewById(R.id.activity_user_login_login);
        buttonLogin.setOnClickListener(this);
    }

    @Override
    protected void initView() {

    }


    @Override
    public void onItemClick(int position) {
        setUserType(position);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.activity_user_login_dropdown:
                showSpinnerWindow();
                break;
            case R.id.activity_user_login_login:
                boolean isNetConnected = NetworkUtils.isNetworkAvailable(UserLoginActivity.this);
                if(!isNetConnected){
                    ShowToast(R.string.network_tips);
                    return;
                }
                login();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void setUserType(int position){
        if(position >= 0 && position <= typeList.size()){
            CustomObject customObject = typeList.get(position);
            textViewType.setText(customObject.toString());
        }
    }

    private void showSpinnerWindow(){
        spinnerPopWindow.setWidth(textViewType.getWidth());
        spinnerPopWindow.showAsDropDown(textViewType);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null && Constants.ACTION_REGISTER_SUCCESS_FINISH.equals(intent.getAction())){
                finish();
            }
        }
    }


    private void login(){
        String userType = textViewType.getText().toString();
        String name = editTextName.getText().toString();
        String password = editTextPassword.getText().toString();

        if(TextUtils.isEmpty(userType)){
            ShowToast(R.string.toast_error_user_type_null);
            return;
        }

        if(TextUtils.isEmpty(name)){
            ShowToast(R.string.toast_error_username_null);
            return;
        }

        if(TextUtils.isEmpty(password)){
            ShowToast(R.string.toast_error_password_null);
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(UserLoginActivity.this);
        progressDialog.setMessage("正在登陆...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        userManager.login(name, password,new SaveListener() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.setMessage("正在获取好友列表...");
                    }
                });
                //更新用户的地理位置以及好友的资料
                updateUserInfo();
                progressDialog.dismiss();
                Intent intent = new Intent(UserLoginActivity.this, HomeActivity.class);
                UserLoginActivity.this.startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                progressDialog.dismiss();
                BmobLog.i(s);
                ShowToast(s);
            }
        });
    }

}
