package com.elight.teaching.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import com.elight.teaching.CustomApplication;
import com.elight.teaching.R;
import com.elight.teaching.entity.multiphotopicker.ImageItem;
import com.elight.teaching.utils.multiphotopicker.CustomConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dawn on 2014/9/5.
 */
public class BaseFragment extends Fragment {

    private int index;
    public BmobUserManager bmobUserManager;
    public BmobChatManager bmobChatManager;
    protected View contentView;
    public LayoutInflater layoutInflater;
    private Handler handler = new Handler();
    public CustomApplication mApplication;
    String targetUrl = null;
    String dateTime;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void runOnWorkThread(Runnable action){
        new Thread(action).start();
    }

    public void runOnUiThread(Runnable action){
        handler.post(action);
    }

    /*照相机获取到的图片或者相册中选择的图片*/
    public static List<ImageItem> dataList = new ArrayList<ImageItem>();
    public static final int REQUEST_CODE_ALBUM = 1;
    public static final int REQUEST_CODE_CAMERA = 2;
    public static String path;

    private PopupWindow mpopupWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        mApplication = CustomApplication.getInstance();
        bmobUserManager = BmobUserManager.getInstance(getActivity());
        bmobChatManager = BmobChatManager.getInstance(getActivity());
        layoutInflater = LayoutInflater.from(getActivity());
    }


    public View findViewById(int paramInt){
        return getView().findViewById(paramInt);
    }

    /*动画启动页面startAnimActivity*/
    public void startAnimActivity(Intent intent){
        this.startActivity(intent);
    }

    public void startAnimActivity(Class<?> cla){
        getActivity().startActivity(new Intent(getActivity(),cla));
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case REQUEST_CODE_CAMERA:{
                if(dataList.size() < CustomConstants.MAX_IMAGE_SIZE && resultCode == -1 && !TextUtils.isEmpty(path)){
                    ImageItem item = new ImageItem();
                    item.sourcePath = path;
                    dataList.add(item);
                }
                break;
            }
            case REQUEST_CODE_ALBUM:{
                String fileName = null;
                if(data != null){
                    Uri originalUri = data.getData();
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    Cursor cursor = contentResolver.query(originalUri, null, null, null, null);
                    if(cursor.moveToFirst()){
                        do{
                            fileName = cursor.getString(cursor.getColumnIndex("_data"));
                        }while (cursor.moveToNext());
                    }
//                    Bitmap bitmap = BitmapUtils.compressImageFromFile(fileName);
//                    targetUrl = CacheUtils.saveToSdCard(getActivity(), bitmap, dateTime);

                    ImageItem item = new ImageItem();
                    item.sourcePath = fileName;
                    dataList.add(item);

                }
                break;
            }

        }

    }


    public void showPopWindow() {
        View view = View.inflate(getActivity(), R.layout.multiphotopicker_popup_windows, null);
        TextView tx_popup_camera = (TextView) view.findViewById(R.id.popup_camera);
        TextView tx_popup_gallery = (TextView) view.findViewById(R.id.popup_gallery);
        TextView tx_popup_cancel = (TextView) view.findViewById(R.id.popup_cancel);

        tx_popup_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeCamera();
                mpopupWindow.dismiss();
            }
        });
        tx_popup_gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(getActivity(), ImageBucketChooseActivity.class);
//                intent.putExtra(CustomConstants.EXTRA_CAN_ADD_IMAGE_SIZE,getAvailableSize());
//                getActivity().startActivity(intent);

                Date date1 = new Date(System.currentTimeMillis());
                dateTime = date1.getTime()+"";
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent, REQUEST_CODE_ALBUM);

                mpopupWindow.dismiss();
            }
        });
        tx_popup_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mpopupWindow.dismiss();
            }
        });

        if (mpopupWindow == null) {
            mpopupWindow = new PopupWindow(getActivity());
            mpopupWindow.setWidth(ViewPager.LayoutParams.MATCH_PARENT);
            mpopupWindow.setHeight(ViewPager.LayoutParams.WRAP_CONTENT);

            mpopupWindow.setFocusable(true);
            mpopupWindow.setOutsideTouchable(true);
        }
        mpopupWindow.setContentView(view);
        mpopupWindow.setFocusable(true);
        mpopupWindow.setTouchable(true);
        mpopupWindow.setOutsideTouchable(true);
        Resources resources =getActivity().getResources();
        Drawable drawable = resources.getDrawable(R.drawable.transparency_background);
        mpopupWindow.setBackgroundDrawable(drawable);
//        mpopupWindow.setBackgroundDrawable(null);
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
        mpopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        mpopupWindow.update();
    }


    public void takeCamera(){
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File vFile = new File(Environment.getExternalStorageDirectory()+"/myimage/",String.valueOf(System.currentTimeMillis())+".jpg");
        if(!vFile.exists()){
            File vDirPath = vFile.getParentFile();
            vDirPath.mkdirs();
        } else {
            if(vFile.exists()){
                vFile.delete();
            }
        }
        path = vFile.getPath();
        Uri cameraUri = Uri.fromFile(vFile);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        this.startActivityForResult(openCameraIntent, REQUEST_CODE_CAMERA);
    }

    public int getAvailableSize(){
        int availSize = CustomConstants.MAX_IMAGE_SIZE - dataList.size();
        if(availSize >= 0){
            return availSize;
        }
        return 0;
    }

}
