package com.elight.teaching.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.elight.teaching.R;

/**
 * Created by dawn on 2014/9/2.
 */
public class UserChooseActivity extends BaseActivity {
    private Button buttonLogin;
    private Button buttonRegister;
    private Button buttonTourist;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_choose);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        buttonLogin = (Button) findViewById(R.id.user_login_button);
        buttonRegister = (Button) findViewById(R.id.user_register_button);
        buttonTourist = (Button) findViewById(R.id.user_tourist_button);
    }

    @Override
    protected void initView() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(UserLoginActivity.class);
//                mIntent=new Intent(UserChooseActivity.this, UserLoginActivity.class);
//                startActivity(mIntent);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(UserRegisterActivity.class);
//                mIntent=new Intent(UserChooseActivity.this, UserRegisterActivity.class);
//                startActivity(mIntent);
            }
        });

        buttonTourist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(HomeActivity.class);
//                mIntent=new Intent(UserChooseActivity.this, HomeActivity.class);
//                startActivity(mIntent);
            }
        });
    }
    //选择不同的用户类型

//    @OnClick(R.id.user_login)
//    private void clickLogin(){
//        Log.d("wangshengyanooooooooooossss","ssssssssssssssssss");
//        mIntent=new Intent(UserChooseActivity.this, UserLoginActivity.class);
//        startActivity(mIntent);
//        openActivity(UserLoginActivity.class);
//    }
//
//
//    @OnClick(R.id.user_register)
//    private void clickRegister(){
//        Log.d("wangshengyanooooooooooossss","ssssssssssssssssss");
//        mIntent=new Intent(UserChooseActivity.this, UserRegisterActivity.class);
//        startActivity(mIntent);
//    }


//    @Override
//    public void onClick(View v) {
//        Log.d("wwwwwwwwwwwwwwwwwwww","oooooooooooooooooooo");
//
//        switch (v.getId()) {
//            case R.id.user_login_button:
//                mIntent=new Intent(UserChooseActivity.this, UserLoginActivity.class);
//                startActivity(mIntent);
//                break;
//            case R.id.user_register_button:
//                mIntent=new Intent(UserChooseActivity.this, UserRegisterActivity.class);
//                startActivity(mIntent);
//                break;
//
//            default:
//                break;
//        }
//    }
}
