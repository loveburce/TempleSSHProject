package com.elight.teaching.activity.multiphotopicker;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.elight.teaching.R;
import com.elight.teaching.activity.BaseActivity;

/**
 * Created by dawn on 2014/10/30.
 */
public class PickerPhotoActivity extends BaseActivity {

    View rootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.multiphotopicker_popup_windows);

        View vi = View.inflate(this, R.layout.multiphotopicker_popup_windows, null);

//        rootView = findViewById(R.layout.multiphotopicker_popup_windows);
        View view =  vi.findViewById(R.id.avatar_change_view);


        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);


        setContentView(view,params);
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {

    }


}
