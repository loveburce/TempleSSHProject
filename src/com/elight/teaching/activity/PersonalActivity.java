package com.elight.teaching.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.elight.teaching.R;
import com.elight.teaching.custom.ExitView;
import com.elight.teaching.utils.ActivityUtils;
import com.elight.teaching.widgets.CustomScrollView;


public class PersonalActivity extends BaseActivity implements OnClickListener {

	private ImageView mBackgroundImageView = null;
	private Button mExitButton;
	private CustomScrollView mScrollView = null;
	private Intent mIntent=null;
	private ExitView exit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_my_nick_page);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mScrollView = (CustomScrollView) findViewById(R.id.personal_scrollView);
		mExitButton=(Button)this.findViewById(R.id.personal_exit);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mScrollView.setImageView(mBackgroundImageView);

		mExitButton.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		//CommonTools.showShortToast(PersonalActivity.this, "稍后开放");
		switch (v.getId()) {
//		case R.id.personal_login_button:
//			mIntent=new Intent(PersonalActivity.this, UserLoginActivity.class);
//			startActivity(mIntent);
//			break;
			
		case R.id.personal_exit:
			
			//实例化SelectPicPopupWindow
			exit = new ExitView(PersonalActivity.this, itemsOnClick);
			//显示窗口
			exit.showAtLocation(PersonalActivity.this.findViewById(R.id.layout_personal), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置


			break;
			
		default:
			break;
		}
		
	}
	
	
	//为弹出窗口实现监听类
    private OnClickListener  itemsOnClick = new OnClickListener(){

		public void onClick(View v) {
			
			switch (v.getId()) {
			case R.id.btn_exit:
                ActivityUtils.showShortToast(PersonalActivity.this, "退出程序");
				
				break;
			case R.id.btn_cancel:
				PersonalActivity.this.dismissDialog(R.id.btn_cancel);
				
				break;
			default:
				break;
			}
		}
    };
	
}
