package com.elight.teaching.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.listener.SaveListener;
import com.elight.teaching.R;
import com.elight.teaching.config.Constants;
import com.elight.teaching.entity.UserInfo;
import com.elight.teaching.utils.NetworkUtils;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dawn on 2014/9/2.
 */
public class UserRegisterActivity extends BaseActivity implements OnWheelChangedListener{

    Button btn_register;
    EditText et_username;
    EditText et_password;
    EditText et_email;
    EditText et_choose_date;
    EditText et_choose_address;
    BmobChatUser chatUser;
    private Dialog mdialog;
    private Calendar calendar = null;
    private PopupWindow mpopupWindow;

    //地理位置选择
    private JSONObject mJsonObject;
    private WheelView mProvince;
    private WheelView mCity;
    private WheelView mArea;
    private String[] mProvinceDatas;
    private Map<String,String[]> mCitisDatasMap = new HashMap<String, String[]>();
    private Map<String,String[]> mAreaDatasMap = new HashMap<String, String[]>();
    private String mCurrentProvinceName;
    private String mCurrentCityName;
    private String mCurrentAreaName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_email = (EditText) findViewById(R.id.et_email);
        et_choose_date = (EditText) findViewById(R.id.et_date);
        et_choose_address = (EditText) findViewById(R.id.et_address);

        btn_register = (Button) findViewById(R.id.btn_register);

    }

    @Override
    protected void initView() {
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        et_choose_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);      //日期弹出框
                int SDKVersion = UserRegisterActivity.this.getSDKVersionNumber();   //获取系统版本
                DatePicker datePicker = findDatePicker((ViewGroup)mdialog.getWindow().getDecorView());      //设置弹出年月日
                if(datePicker != null){
                    // 设置弹出年，隐藏月和日，getChildAt(0)为年，getChildAt(1)为月，getChildAt(2)为日
                    if(SDKVersion < 11){
//                        ((ViewGroup) datePicker.getChildAt(0)).getChildAt(1).setVisibility(View.GONE);

                        ((ViewGroup) datePicker.getChildAt(0)).getChildAt(1).setVisibility(View.VISIBLE);
                    } else if(SDKVersion > 14){
//                        ((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)) .getChildAt(1).setVisibility(View.GONE);

                        ((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)) .getChildAt(1).setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        et_choose_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopMenu();
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) { // 对应上面的showDialog(0);//日期弹出框
        mdialog = null;
        switch (id) {
            case 0:
                calendar = Calendar.getInstance();
                mdialog = new CustomerDatePickerDialog(this,//CustomerDatePickerDialog--DatePickerDialog互换
                        new CustomerDatePickerDialog.OnDateSetListener() {//CustomerDatePickerDialog--DatePickerDialog互换
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                textview.setText(year + "-" + (monthOfYear + 1));
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                break;
        }
        return mdialog;
    }

    private void register(){
        String name = et_username.getText().toString();
        String password = et_password.getText().toString();
        String pwd_again = et_email.getText().toString();

        if(TextUtils.isEmpty(name)){
            ShowToast(R.string.toast_error_username_null);
            return;
        }
        if(TextUtils.isEmpty(password)){
            ShowToast(R.string.toast_error_password_null);
            return;
        }
        if(!pwd_again.equals(password)){
            ShowToast(R.string.toast_error_comfirm_password);
        }

        boolean isNetConnected = NetworkUtils.isNetworkAvailable(this);
        if(!isNetConnected){
            ShowToast(R.string.network_tips);
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(UserRegisterActivity.this);
        progressDialog.setMessage("正在注册...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        //注册的时候需要注意两点： 1.User表中绑定的ID和type。2.设备表中绑定的username字段

        final UserInfo userInfo = new UserInfo();
        userInfo.setUsername(name);
        userInfo.setPassword(password);
        //将user和设备的ID进行绑定
        userInfo.setDeviceType("android");
        userInfo.setInstallId(BmobInstallation.getInstallationId(UserRegisterActivity.this));
        userInfo.signUp(UserRegisterActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                ShowToast("注册成功！");
                //将设备与username进行绑定
                userManager.bindInstallationForRegister(userInfo.getUsername());
                //更新地理位置信息
                updateUserLocation();

                //发广播通知登录页面退出
                sendBroadcast(new Intent(Constants.ACTION_REGISTER_SUCCESS_FINISH));
                //启动主页
                Intent intent = new Intent(UserRegisterActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                BmobLog.i(s);
                ShowToast("注册失败:" + s);
                progressDialog.dismiss();
            }
        });
    }


    /**
     * 发现标题栏也要改，通过查看DatePickerDialog源码，需要自定义并实现onDateChanged方法才可实现
     * CustomerDatePickerDialog--DatePickerDialog互换
     *  */
    class CustomerDatePickerDialog extends DatePickerDialog {

        public CustomerDatePickerDialog(Context context,
                                        OnDateSetListener callBack,
                                        int year, int monthOfYear, int dayOfMonth) {
            super(context, callBack, year, monthOfYear, dayOfMonth);
        }

        @Override
        public void onDateChanged(DatePicker view, int year, int month, int day) {
            super.onDateChanged(view, year, month, day);
            mdialog.setTitle(year + "年" + month +"月" + day +"日");
        }
    }

    /**
     * 从当前Dialog中查找DatePicker子控件
     *
     * @param group
     * @return
     */
    private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }

    /**
     * 获取系统SDK版本
     *
     * @return
     */
    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }

    private void showPopMenu(){
        View view = View.inflate(UserRegisterActivity.this, R.layout.popup_choose_citys,null);
        mProvince = (WheelView) view.findViewById(R.id.id_province);
        mCity = (WheelView) view.findViewById(R.id.id_city);
        mArea = (WheelView) view.findViewById(R.id.id_area);
        initJsonData();
        initDatas();

        mProvince.setViewAdapter(new ArrayWheelAdapter<String>(UserRegisterActivity.this, mProvinceDatas));
        mProvince.addChangingListener(this);
        mCity.addChangingListener(this);
        mArea.addChangingListener(this);

        mProvince.setVisibleItems(5);
        mCity.setVisibleItems(5);
        mArea.setVisibleItems(5);

        updateCities();
        updateAreas();

        if (mpopupWindow == null) {
            mpopupWindow = new PopupWindow(UserRegisterActivity.this);
            mpopupWindow.setWidth(ViewPager.LayoutParams.MATCH_PARENT);
            mpopupWindow.setHeight(ViewPager.LayoutParams.WRAP_CONTENT);

            mpopupWindow.setFocusable(true);
            mpopupWindow.setOutsideTouchable(true);
        }
        mpopupWindow.setContentView(view);
        mpopupWindow.setFocusable(true);
        mpopupWindow.setTouchable(true);
        mpopupWindow.setOutsideTouchable(true);
        Resources resources = UserRegisterActivity.this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.transparency_background);
        mpopupWindow.setBackgroundDrawable(drawable);
        mpopupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mpopupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        mpopupWindow.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
        mpopupWindow.update();
    }

//    private class OnWheelChangedListener implements kankan.wheel.widget.OnWheelChangedListener{
//
//        @Override
//        public void onChanged(WheelView wheel, int oldValue, int newValue) {
//            if(wheel == mProvince){
//                updateCities();
//            } else if(wheel == mCity){
//                updateAreas();
//            } else if(wheel == mArea){
//                mCurrentAreaName = mAreaDatasMap.get(mCurrentCityName)[newValue];
//            }
//        }
//    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if(wheel == mProvince){
            updateCities();
        } else if(wheel == mCity){
            updateAreas();
        } else if(wheel == mArea){
            mCurrentAreaName = mAreaDatasMap.get(mCurrentCityName)[newValue];
        }
    }

    private void updateAreas(){
        int pCurrent = mCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProvinceName)[pCurrent];
        String[] areas = mAreaDatasMap.get(mCurrentCityName);

        if(areas == null){
            areas = new String[]{""};
        }
        mArea.setViewAdapter(new ArrayWheelAdapter<String>(UserRegisterActivity.this, areas));
        mArea.setCurrentItem(0);
    }

    private void updateCities(){
        int pCurrent = mProvince.getCurrentItem();
        mCurrentProvinceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProvinceName);
        if(cities == null){
            cities = new String[]{""};
        }
        mCity.setViewAdapter(new ArrayWheelAdapter<String>(UserRegisterActivity.this,cities));
        mCity.setCurrentItem(0);
        updateAreas();
    }

    private void initDatas(){
        try{
            JSONArray jsonArray = mJsonObject.getJSONArray("citylist");
            mProvinceDatas = new String[jsonArray.length()];
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObjectP = jsonArray.getJSONObject(i);
                String province = jsonObjectP.getString("p");

                mProvinceDatas[i] = province;

                JSONArray jsonArrayCs = null;

                try{
                    /*Throws JSONException if the mapping doesn't exist or is not a JSONArray*/
                    jsonArrayCs = jsonObjectP.getJSONArray("c");
                }catch (Exception e){
                    continue;
                }

                String[] mCitiesDatas = new String[jsonArrayCs.length()];
                for(int j=0; j<jsonArrayCs.length(); j++){
                    JSONObject jsonCity = jsonArrayCs.getJSONObject(j);
                    String city = jsonCity.getString("n");
                    mCitiesDatas[j] = city;
                    JSONArray jsonAreas = null;
                    try{
                        /*Throws JSONException if the mapping doesn't exist or is not a JSONArray*/
                        jsonAreas = jsonCity.getJSONArray("a");
                    } catch (Exception e){
                        continue;
                    }

                    String[] mAreasDatas = new String[jsonAreas.length()];
                    for(int k=0; k<jsonAreas.length(); k++){
                        String area = jsonAreas.getJSONObject(k).getString("s");
                        mAreasDatas[k] = area;
                    }
                    mAreaDatasMap.put(city, mAreasDatas);
                }
                mCitisDatasMap.put(province,mCitiesDatas);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        mJsonObject = null;
    }

    private void initJsonData(){
        try{
            StringBuffer stringBuffer = new StringBuffer();
            InputStream inputStream = getAssets().open("city.json");
            int len = -1;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) != -1){
                stringBuffer.append(new String(bytes, 0, len,"gbk"));
            }
            inputStream.close();
            mJsonObject = new JSONObject(stringBuffer.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showChoose(View view)
    {
//        Toast.makeText(UserRegisterActivity.this, mCurrentProviceName + mCurrentCityName + mCurrentAreaName, Toast.LENGTH_SHORT).show();
    }
}
